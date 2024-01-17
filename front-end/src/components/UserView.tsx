import {ManageUser} from "./ManageUser.tsx";
import {useUserContext} from "../Context/UserProvider.tsx";
import {useEffect, useState} from "react";
import {Account} from "../types/Account.ts";
import {api} from "../api/api.ts";

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
            <h1>Hi</h1>
            <ManageUser key={`/manage/${userAcc?.personalId}/client`}/>
        </>
    );
}