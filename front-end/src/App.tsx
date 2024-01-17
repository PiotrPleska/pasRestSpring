import {BrowserRouter, Route, Routes, Outlet, Link} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import {ManageUser} from "./components/ManageUser.tsx";
import {UserAddForm} from "./components/UserAddForm.tsx";
import ClientRents from "./components/ClientRents.tsx";
import {RentAddForm} from "./components/RentAddForm.tsx";
import {HomePage} from "./components/HomePage.tsx";
import UsersList from "./components/UsersList.tsx";
import {UserContextProvider, useUserContext} from "./Context/UserProvider.tsx";
import {UserView} from "./components/UserView.tsx";

const clearStorage = () => {
    localStorage.clear();
    console.log("Storage cleared");
    window.location.reload();
    window.location.href = "/";
}

function App() {
    return (
        <BrowserRouter>
            <UserContextProvider>
                <Routes>
                    <Route element={<Layout/>}>
                        <Route path="/" element={<HomePage/>}/>
                        <Route path={`/manage/:personalId/:accountType`} element={<ManageUser/>}/>
                        <Route path={`/rents/:accountId`} element={<ClientRents/>}/>
                        <Route path={'/addAccount'} element={<UserAddForm/>}/>
                        <Route path={'/addRent/:accountId'} element={<RentAddForm/>}/>
                        <Route path={'/UsersList'} element={<UsersList/>}/>
                        <Route path={'/Home'} element={<UserView/>}/>
                    </Route>
                </Routes>
            </UserContextProvider>
        </BrowserRouter>
    );
}

function Layout() {
    const {user} = useUserContext();
    return (
        <div className="App">
            <nav className="navbar">
                <ul className="nav-list">
                    <li className="nav-item">
                        <Link to="/">Home</Link>
                    </li>
                    <li className="nav-item">
                        <Link to={`/usersList`}>User List</Link>
                    </li>
                    <li className="nav-item">
                        <Link to={'/addAccount'}>Add User</Link>
                    </li>
                    <li>
                        {user ? (
                            <Link to={'/'} onClick={clearStorage}>Log out</Link>
                        ) : (
                            <Link to={'/'}>Log in</Link>
                        )}
                    </li>
                    <li>
                        {user ? (
                                <p>
                                    {user.login}
                                </p>
                            ) : (
                            <p>
                            Not logged in
                            </p>
                            )
                        }
                    </li>
                </ul>
            </nav>
            <Outlet/>
        </div>
    );
}

export default App;
