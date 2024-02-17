import type {RentPost} from "../types/Rent.ts";
import {useForm} from "react-hook-form"
import {format} from "date-fns";

import {api} from "../api/api.ts";
import {Link, useParams} from "react-router-dom";
import {Room} from "../types/Room.ts";
import {useEffect, useState} from "react";
import {useUserContext} from "../Context/UserProvider.tsx";
import {AccountTypeEnum} from "../enums/AccountType.enum.ts";

export function RentAddForm() {
    const {user} = useUserContext();
    const {accountId} = useParams<{ accountId: string }>();
    const {
        register,
        handleSubmit,
        formState: {errors},
        setValue,
    } = useForm<RentPost>();

    const [rooms, setRooms] = useState<Array<Room>>([]);
    const [selectedDate, setSelectedDate] = useState<Date | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [data, setData] = useState<RentPost | null>(null);


    useEffect(() => {
        const fetchRooms = async () => {
            try {
                const response = await api.getRooms();
                const roomData = response.data;
                setRooms(roomData);
            } catch (error) {
                console.error("Error fetching rooms:", error);
            }
        };

        fetchRooms();
    }, []);

    const handleDateChange = (date: Date | null) => {
        setSelectedDate(date);
        setValue("rentStartDate", date ? format(date, "yyyy-MM-dd'T'HH:mm:ss") : "");
    };

    const onSubmit = async (formData: RentPost) => {
        setData(formData);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    const handleConfirmSubmit = async () => {
        setShowModal(false);

        try {
            const rentData: RentPost = {
                ...data,
                accountId: accountId ?? '',
                rentStartDate: selectedDate ? format(selectedDate, "yyyy-MM-dd'T'HH:mm:ssX") : "",
                roomId: data?.roomId ?? '',
            };
            console.log(rentData);
            await api.addRent(rentData).then((r) => {
                if (r.status === 200) {
                    alert("Rent added successfully!")
                }
            }).catch((e) => {
                alert(e.response.data)
                }
            );
        } catch (error) {
            alert(error)
        }
    };


    return (
        <>
            {user?.accountType === AccountTypeEnum.CLIENT && <Link to="/Home">User Home</Link>}
            {user?.accountType === AccountTypeEnum.RESOURCE_MANAGER && <Link to="/UsersList">Users List</Link>}
            <h1>Add Rent</h1>
            <form onSubmit={handleSubmit(onSubmit)}>
                <label htmlFor="roomId">Select Room</label>
                <select
                    id="roomId"
                    {...register("roomId", {required: true})}
                    onChange={(e) => setValue("roomId", e.target.value)}
                >
                    <option value="">
                        Choose a room
                    </option>
                    {rooms.map((room) => (
                        <option key={room.id} value={room.id}>
                            {room.roomNumber}
                        </option>
                    ))}
                </select>
                {errors.roomId && <span>{errors.roomId.message}</span>}

                <label htmlFor="rentStartDate">Start Date</label>
                <input
                    id="rentStartDate"
                    type="datetime-local"
                    {...register("rentStartDate", {required: true})}
                    onChange={(e) => handleDateChange(new Date(e.target.value))}
                />
                {errors.rentStartDate && <span>This field is required</span>}

                <button type="submit">Submit</button>
            </form>

            {showModal && (
                <div>
                    <div className="modal-content">
                        <p>Are you sure you want to add this rent?</p>
                        <button onClick={handleConfirmSubmit}>Yes</button>
                        <button onClick={handleCloseModal}>No</button>
                    </div>
                </div>
            )}
        </>
    );
}