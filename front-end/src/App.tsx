import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import {ManageUser} from "./components/ManageUser.tsx";
import {UserAddForm} from "./components/UserAddForm.tsx";
import ClientRents from "./components/ClientRents.tsx";
import {RentAddForm} from "./components/RentAddForm.tsx";
import {HomePage} from "./components/HomePage.tsx";
import UsersList from "./components/UsersList.tsx";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage/>}/>
                <Route path={`/manage/:personalId/:accountType`} element={<ManageUser/>}/>
                <Route path={`/rents/:accountId`} element={<ClientRents/>}/>
                <Route path={'/addAccount'} element={<UserAddForm/>}/>
                <Route path={'/addRent/:accountId'} element={<RentAddForm/>}/>
                <Route path={'/usersList'} element={<UsersList/>}/>
            </Routes>
        </Router>
    );
}

export default App;
