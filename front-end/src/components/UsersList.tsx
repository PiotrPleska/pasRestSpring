import {api} from "../api/api.ts";
import type {Account} from "../types/Account.ts";
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {useUserContext} from "../Context/UserProvider.tsx";
import {AccountTypeEnum} from "../enums/AccountType.enum.ts";


const UsersList = () => {
    const [clients, setClients] = useState(new Array<Account>());
    const [admins, setAdmins] = useState(new Array<Account>());
    const [resourceManagers, setResourceManagers] = useState(new Array<Account>());
    const [filteredClients, setFilteredClients] = useState(new Array<Account>());
    const [filteredAdmins, setFilteredAdmins] = useState(new Array<Account>());
    const [filteredResourceManagers, setFilteredResourceManagers] = useState(new Array<Account>());
    const {user} = useUserContext();

    const filterUsers = (clients: Account[], admins: Account[], resourceManagers: Account[], search: string) => {
        if (search === "" || search === undefined || search === null) {
            setFilteredClients(clients);
            setFilteredAdmins(admins);
            setFilteredResourceManagers(resourceManagers);
        }
        setFilteredClients(clients.filter((client) => client.id?.includes(search)));
        setFilteredAdmins(admins.filter((admin) => admin.id?.includes(search)));
        setFilteredResourceManagers(resourceManagers.filter((resourceManager) => resourceManager.id?.includes(search)));
    }

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                // const response = await api.getAccounts();
                const clientsResponse = await api.getClientAccounts();
                const adminsResponse = await api.getAdminAccounts();
                const resourceManagersResponse = await api.getResourceManagerAccounts();
                const clientsData = clientsResponse.data;
                setClients(clientsData);
                const adminsData = adminsResponse.data;
                setAdmins(adminsData);
                const resourceManagersData = resourceManagersResponse.data;
                setResourceManagers(resourceManagersData);
                filterUsers(clientsData, adminsData, resourceManagersData, "");

            } catch (error) {
                console.error("Error fetching users:", error);
            }
        };

        fetchUsers();
    }, []);

    // const authenticate = async () => {
    //     try {
    //         const response = await api.authenticate("korwinkrul123","nowehaslo123");
    //         if (response.status === 200) {
    //             console.log("Authenticated");
    //         }
    //     } catch (error) {
    //         console.error("Error authenticating:", error);
    //     }
    // }

    return (
        <>
            <br/>
            <input
                type="text"
                placeholder="Search by user ID"
                onChange={(e) => {
                    filterUsers(clients, admins, resourceManagers, e.target.value);
                }}
            />
            {filterUsers.length > 0 && (
                <table className="table table-dark">
                    <thead>
                    <tr>
                        <th>User ID</th>
                        <th>Login</th>
                        <th>Personal ID</th>
                        <th>Active</th>
                        <th>Account Type</th>
                        <th></th>
                        <th>Action</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredClients.map((account) => (
                        <tr key={account.id}>
                            <td>{account.id}</td>
                            <td>{account.login}</td>
                            <td>{account.personalId}</td>
                            <td>{account.active ? "Yes" : "No"}</td>
                            <td>CLIENT</td>
                            <td>
                                {user?.accountType === AccountTypeEnum.ADMIN ? (
                                <Link to={`/manage/${account.personalId}/client`}>Manage</Link>
                                ) : (
                                    <></>
                                )}
                            </td>
                            <td>
                                {user?.accountType === AccountTypeEnum.RESOURCE_MANAGER ? (
                                    <Link to={`/rents/${account.id}`}>Check Rents</Link>
                                ) : (
                                    <></>
                                )}
                            </td>

                            <td>
                                {user?.accountType === AccountTypeEnum.RESOURCE_MANAGER ? (
                                    <Link to={`/addRent/${account.id}`}>Add Rent</Link>
                                ) : (
                                    <></>
                                )}
                            </td>
                        </tr>
                    ))}
                    {filteredAdmins.map((account) => (
                        <tr key={account.id}>
                            <td>{account.id}</td>
                            <td>{account.login}</td>
                            <td>{account.personalId}</td>
                            <td>{account.active ? "Yes" : "No"}</td>
                            <td>ADMIN</td>
                            <td>
                                {user?.accountType === AccountTypeEnum.ADMIN ? (
                                    <Link to={`/manage/${account.personalId}/client`}>Manage</Link>
                                ) : (
                                    <></>
                                )}
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                    ))}
                    {filteredResourceManagers.map((account) => (
                        <tr key={account.id}>
                            <td>{account.id}</td>
                            <td>{account.login}</td>
                            <td>{account.personalId}</td>
                            <td>{account.active ? "Yes" : "No"}</td>
                            <td>RESOURCE MANAGER</td>
                            <td>
                                {user?.accountType === AccountTypeEnum.ADMIN ? (
                                    <Link to={`/manage/${account.personalId}/client`}>Manage</Link>
                                ) : (
                                    <></>
                                )}
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </>

    );
};

export default UsersList;
