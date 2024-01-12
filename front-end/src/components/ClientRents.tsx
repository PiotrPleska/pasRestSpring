import { Link, useParams } from "react-router-dom";
import { api } from "../api/api.ts";
import { useEffect, useState } from "react";
import { RentGet } from "../types/Rent.ts";

const ClientRents = () => {
    const { accountId } = useParams<{ accountId: string }>();
    const [rents, setRents] = useState<Array<RentGet>>([]);
    const [showModal, setShowModal] = useState(false);
    const [currentRentId, setCurrentRentId] = useState<string | null>(null);


    const fetchRents = async () => {
        try {
            const response = await api.getUserRents(accountId);
            const data = response.data;
            setRents(data);
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    };

    useEffect(() => {
        fetchRents();
    }, [accountId]);

    const handleEndRent = async (rentId: string) => {
        setCurrentRentId(rentId);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setCurrentRentId(null);
    };

    const handleConfirmSubmit = async () => {
        if (currentRentId) {
            try {
                await api.endRent(currentRentId);
                handleCloseModal();
                await fetchRents();
            } catch (error) {
                console.error("Error ending rent:", error);
            }
        }
    };

    return (
        <>
            <Link to="/">Home</Link>
            <br />
            {rents.length > 0 && (
                <table className="table table-dark">
                    <thead>
                    <tr>
                        <th>Rent Date</th>
                        <th>Return Date</th>
                        <th>Account's Login</th>
                        <th>Room Number</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {rents.map((rent) => (
                        <tr key={rent.id}>
                            <td>{rent.rentStartDate}</td>
                            <td>{rent.rentEndDate !== undefined ? rent.rentEndDate : "Not returned"}</td>
                            <td>{rent.account.login}</td>
                            <td>{rent.room.roomNumber}</td>
                            <td>
                                {(rent.rentEndDate === "" || rent.rentEndDate === undefined || rent.rentEndDate == null) && (
                                    <button onClick={() => handleEndRent(rent.id)}>End Rent</button>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}

            {showModal && (
                <div>
                    <div className="modal-content">
                        <p>Are you sure you want to end this rent?</p>
                        <button onClick={handleConfirmSubmit}>Yes</button>
                        <button onClick={handleCloseModal}>No</button>
                    </div>
                </div>
            )}


        </>
    );
};

export default ClientRents;
