import {useState} from "react";
import {api} from "../api/api.ts";
import {Account} from "../types/Account.ts";
import {useForm} from "react-hook-form";
import * as yup from 'yup';
import {yupResolver} from '@hookform/resolvers/yup'

interface UpdatePasswordProps {
    user: Account | undefined,
    accountType: string | undefined,
    setUser: (value: (((prevState: (Account | undefined)) => (Account | undefined)) | Account | undefined)) => void
}

export const UpdatePassword = ({user, setUser, accountType}: UpdatePasswordProps) => {

    const [password, setPassword] = useState<string>('');
    const [showModal, setShowModal] = useState(false);

    const schema = yup.object({
        password: yup.string().required('Password is required').min(6, 'Must be at least 2 characters').max(20, 'Must be less than 20 characters'),
    })

    const {register, handleSubmit, formState: {errors}} = useForm(
        {
            resolver: yupResolver(schema),
        }
    );

    const handleOpenModal = () => {
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    const handleConfirmSubmit = () => {
        if (user) {
            const updatedUser = {
                login: user.login,
                password: password,
                personalId: user.personalId,
            };
            if (accountType == 'client')
                api.updateClientPassword(updatedUser, user?.login ?? "").then((r) => console.log(r));
            else if (accountType == 'admin')
                api.updateAdminPassword(updatedUser, user?.login ?? "").then((r) => console.log(r));
            else if (accountType == 'resourceManager')
                api.updateResourceManagerPassword(updatedUser, user?.login ?? "").then((r) => console.log(r));
            handleCloseModal();
        }
    };

    return (
        <>
            {/* Add password input field */}
            <div>
                <form onSubmit={handleSubmit(handleOpenModal)}>
                    <label>
                        Password:
                        <input
                            type="password"
                            value={password}
                            {...register('password', {required: true})}
                            onChange={(e) => {
                                user && setUser({...user, password: e.target.value});
                                setPassword(e.target.value);
                            }}
                        />
                        {errors.password && <span style={{color: 'red'}}>{errors.password.message}</span>}
                    </label>
                    <button type="submit">
                        Update Password
                    </button>
                </form>

                {showModal && (
                    <div>
                        <div className="modal-content">
                            <p>Are you sure you want to update the password?</p>
                            <button onClick={handleConfirmSubmit}>Yes</button>
                            <button onClick={handleCloseModal}>No</button>
                        </div>
                    </div>
                )}
            </div>
        </>
    );
};