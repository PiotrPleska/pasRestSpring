import {BrowserRouter as Router, Link, Route, Routes} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import UsersList from './components/UsersList.tsx';
import {ManageUser} from "./components/ManageUser.tsx";
import {UserAddForm} from "./components/UserAddForm.tsx";
import ClientRents from "./components/ClientRents.tsx";
import {RentAddForm} from "./components/RentAddForm.tsx";


function Home() {
    return (
        <>

            <h1>
                <i>Hot</i>el
            </h1>
            <Link to={`/addAccount`}>Add Account</Link>
            <UsersList/>
        </>
    );
}

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path={`/manage/:personalId/:accountType`} element={<ManageUser/>}/>
                <Route path={`/rents/:accountId`} element={<ClientRents/>}/>
                <Route path={'/addAccount'} element={<UserAddForm/>}/>
                <Route path={'/addRent/:accountId'} element={<RentAddForm/>}/>
            </Routes>
        </Router>
    );
}

export default App;
