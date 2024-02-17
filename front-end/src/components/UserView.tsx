import {useUserContext} from "../Context/UserProvider.tsx";
import {useEffect, useState} from "react";
import {Account} from "../types/Account.ts";
import {api} from "../api/api.ts";
import {Link} from "react-router-dom";
import {UpdatePassword} from "./UpdatePassword.tsx";
import {AccountTypeEnum} from "../enums/AccountType.enum.ts";

export function UserView() {


    const {user} = useUserContext();
    const [userAcc, setUserAcc] = useState<Account>();

    useEffect(() => {
        const fetchUser = async () => {
            const respone = await api.getAccountByLogin(user?.login);
            setUserAcc(respone.data);
        }
        fetchUser();
    }, []);





    return (
        <>
            <h1>User Page</h1>
            <p>ID: {userAcc?.id} </p>
            <p>Login: {userAcc?.login}</p>
            <p>Personal ID: {userAcc?.personalId}</p>

            <UpdatePassword user={userAcc} setUser={setUserAcc} accountType="client" />
            {user?.accountType === AccountTypeEnum.CLIENT && <Link to={`/rents/${userAcc?.id}`}>Check Rents</Link>}
            <br/>
            {user?.accountType === AccountTypeEnum.CLIENT && <Link to={`/addRent/${userAcc?.id}`}>Add Rent</Link>}


        </>
    );
}