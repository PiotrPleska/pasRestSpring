import {Link, useParams} from 'react-router-dom';
import {useEffect, useState} from 'react';
import type {Account} from '../types/Account.ts';
import {api} from '../api/api.ts';
import {Controller, useForm} from 'react-hook-form';
import {UpdatePassword} from "./UpdatePassword.tsx";


export function ManageUser() {

    const {personalId, accountType} = useParams<{ personalId: string, accountType: string }>();
    // const {accountType} = useParams<{ accountType: string }>();
    const [user, setUser] = useState<Account>();
    const [radioValue, setRadioValue] = useState<boolean>(false);
    const {control, handleSubmit} = useForm();
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await api.getAccount(personalId ?? '');
                const data = response.data;

                setUser(data);

                setRadioValue(data?.active ?? false);
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        };

        fetchUser();
    }, [personalId]);

    const onSubmit = async () => {
        console.log('submit');
        setShowModal(true);
    };

    const handleRadioChange = (value: boolean) => {
        setRadioValue(value);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    const handleConfirmSubmit = async () => {
        try {
            if (radioValue) {
                await api.activateAccount(user?.login ?? '');
            } else {
                await api.deactivateAccount(user?.login ?? '');
            }
            // Add any additional logic or redirects after account activation
            handleCloseModal();
        } catch (error) {
            console.error('Error activating/deactivating account:', error);
        }
    };


    return (
        <>
            <Link to="/usersList">User List</Link>
            <h1>User Page</h1>
            <p>ID: {personalId} </p>
            <p>Login: {user?.login}</p>
            <p>Personal ID: {user?.personalId}</p>
            <p>Account Type: {accountType}</p>

            {/* Add the form with react-hook-form */}
            <form onSubmit={handleSubmit(onSubmit)}>
                {/* Add radio button for activation */}
                <div>
                    <label>
                        Activate Account: &nbsp;
                        <Controller
                            control={control}
                            name="active"
                            render={({ field }) => (
                                <input
                                    type="radio"
                                    {...field}
                                    checked={radioValue === true}
                                    onChange={() => handleRadioChange(true)}
                                />
                            )}
                        />{' '}
                        Yes
                    </label>
                    <label>
                        <Controller
                            control={control}
                            name="active"
                            render={({ field }) => (
                                <input
                                    type="radio"
                                    {...field}
                                    checked={radioValue === false}
                                    onChange={() => handleRadioChange(false)}
                                />
                            )}
                        />{' '}
                        No
                    </label>
                </div>

                {/* Add submit button */}
                <button type="submit">Submit</button>
            </form>

            {showModal && (
                <div>
                    <div className="modal-content">
                        <p>Are you sure you want to perform this action?</p>
                        <button onClick={handleConfirmSubmit}>Yes</button>
                        <button onClick={handleCloseModal}>No</button>
                    </div>
                </div>
            )}

            <UpdatePassword user={user} setUser={setUser} accountType={accountType} />
        </>
    );
}
