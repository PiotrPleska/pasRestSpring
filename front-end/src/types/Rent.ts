import {Account} from "./Account.ts";
import {Room} from "./Room.ts";

export interface RentPost {
    accountId: string;
    roomId: string;
    rentStartDate: string;
}

export interface RentGet {
    id: string;
    rentStartDate: string;
    rentEndDate: string;
    account: Account;
    room: Room;
}