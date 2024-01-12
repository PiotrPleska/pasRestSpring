import {useForm} from "react-hook-form"
import * as yup from 'yup';
import {yupResolver} from '@hookform/resolvers/yup'

import {api} from "../api/api.ts";
import {Link} from "react-router-dom";
import {useState} from "react";
import {AccountTypeEnum} from "../enums/AccountType.enum.ts";

const schema = yup.object({
    login: yup.string().required('Last Name is required').min(6, 'Must be at least 2 characters').max(20, 'Must be less than 20 characters'),
    password: yup.string().required('Password is required').min(6, 'Must be at least 2 characters').max(20, 'Must be less than 20 characters'),
    personalId: yup.string().required('Personal ID is required').min(11, 'Must be exactly 11 characters').max(11, 'Must be exactly 11 characters'),
    accountType: yup.string().oneOf(Object.values(AccountTypeEnum)).required('Account type is required'),
})

type AddUserFormType = yup.InferType<typeof schema>;

export function UserAddForm() {
    const {
        register,
        handleSubmit,
        formState: {errors},
    } = useForm<AddUserFormType>({
            resolver: yupResolver(schema),
        }
    );

    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState<AddUserFormType | null>(null);
    const handleOpenModal = (data: AddUserFormType) => {
        setFormData(data);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };
    const handleConfirmSubmit = () => {
        if (formData) {
            if(formData.accountType === 'CLIENT')
                api.addClientAccount(formData).then((r) => console.log(r));
            else if(formData.accountType === 'ADMIN')
                api.addAdminAccount(formData).then((r) => console.log(r));
            else if(formData.accountType === 'RESOURCE_MANAGER')
                api.addResourceManagerAccount(formData).then((r) => console.log(r));
            handleCloseModal();
        }
    };

    return (
        <>
            <Link to="/">Home</Link>
            <h1>Add User</h1>
            <form className="form-label" onSubmit={handleSubmit((data) => handleOpenModal(data))}>
                <label htmlFor="login">Login</label>
                <input id="login" type="text" {...register('login', { required: true })} />
                <br/>
                {errors.login && <span style={{ color: 'red' }}>{errors.login.message}</span>}
                <br />
                <label htmlFor="password">Password</label>
                <input id="password" type="password" {...register('password', { required: true })} />
                <br/>
                {errors.password && <span style={{ color: 'red' }}>{errors.password.message}</span>}
                <br />
                <label htmlFor="personalId">Personal ID</label>
                <input id="personalId" type="string" {...register('personalId', { required: true })} />
                <br/>
                {errors.personalId && <span style={{ color: 'red' }}>{errors.personalId.message}</span>}
                <br />
                <label htmlFor="accountType">Select Account Type</label>
                <select
                    id="roomId"
                    {...register("accountType", {required: true})}
                >
                    <option value="CLIENT">Client</option>
                    <option value="ADMIN">Admin</option>
                    <option value="RESOURCE_MANAGER">Resource Manager</option>
                </select>
                <br />
                <button type="submit">Submit</button>
            </form>

            {showModal && (
                <div>
                    <div className="modal-content">
                        <p>Are you sure you want to add this user?</p>
                        <button onClick={handleConfirmSubmit}>Yes</button>
                        <button onClick={handleCloseModal}>No</button>
                    </div>
                </div>
            )}
        </>
    );
}