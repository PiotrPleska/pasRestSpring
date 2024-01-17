import {BrowserRouter, Route, Routes, Outlet, Link} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import {ManageUser} from "./components/ManageUser.tsx";
import {UserAddForm} from "./components/UserAddForm.tsx";
import ClientRents from "./components/ClientRents.tsx";
import {RentAddForm} from "./components/RentAddForm.tsx";
import {HomePage} from "./components/HomePage.tsx";
import UsersList from "./components/UsersList.tsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route element={<Layout />}>
                    <Route path="/" element={<HomePage/>}/>
                    <Route path={`/manage/:personalId/:accountType`} element={<ManageUser/>}/>
                    <Route path={`/rents/:accountId`} element={<ClientRents/>}/>
                    <Route path={'/addAccount'} element={<UserAddForm/>}/>
                    <Route path={'/addRent/:accountId'} element={<RentAddForm/>}/>
                    <Route path={'/usersList'} element={<UsersList/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

function Layout() {
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
                </ul>
            </nav>
            <Outlet />
        </div>
    );
}

export default App;
