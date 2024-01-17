import {api} from "../api/api.ts";
import type {Account} from "../types/Account.ts";
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {useUserContext} from "../Context/UserProvider.tsx";


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
                const adminsData = adminsResponse.data;
                const resourceManagersData = resourceManagersResponse.data;
                setClients(clientsData);
                setAdmins(adminsData);
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
                    {filteredClients.map((user) => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.login}</td>
                            <td>{user.personalId}</td>
                            <td>{user.active ? "Yes" : "No"}</td>
                            <td>CLIENT</td>
                            <td>
                                <Link to={`/manage/${user.personalId}/client`}>Manage</Link>
                            </td>
                            <td>
                                <Link to={`/rents/${user.id}`}>Check Rents</Link>
                            </td>
                            <td><Link to={`/addRent/${user.id}`}>Add Rent</Link></td>
                        </tr>
                    ))}
                    {filteredAdmins.map((user) => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.login}</td>
                            <td>{user.personalId}</td>
                            <td>{user.active ? "Yes" : "No"}</td>
                            <td>ADMIN</td>
                            <td>
                                <Link to={`/manage/${user.personalId}/admin`}>Manage</Link>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                    ))}
                    {filteredResourceManagers.map((user) => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.login}</td>
                            <td>{user.personalId}</td>
                            <td>{user.active ? "Yes" : "No"}</td>
                            <td>RESOURCE MANAGER</td>
                            <td>
                                <Link to={`/manage/${user.personalId}/resourceManager`}>Manage</Link>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
            <p>
                {user?.login}
            </p>
        </>

    );
};

export default UsersList;
