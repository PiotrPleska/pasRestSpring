import axios from 'axios'
import {ApiResponseType} from "../types/ApiResponse.ts";
import {Account} from "../types/Account.ts";
import {RentGet, RentPost} from "../types/Rent.ts";
import {Room} from "../types/Room.ts";

export const API_URL = "http://localhost:8080/api"
export const TIMEOUT_IN_MS = 30000
export const DEFAULT_HEADERS = {
    Accept: 'application/json',
    'Content-type': 'application/json',
}

export const apiWithConfig = axios.create({
    baseURL: API_URL,
    timeout: TIMEOUT_IN_MS,
    headers: DEFAULT_HEADERS,
})

export const api = {
    getAccounts: (): ApiResponseType<Array<Account>> => apiWithConfig.get("/accounts"),
    getAccount: (id: string): ApiResponseType<Account> => apiWithConfig.get(`/accounts/personal-id/${id}`),
    getClientAccounts: (): ApiResponseType<Array<Account>> => apiWithConfig.get("/accounts/clients"),
    getAdminAccounts: (): ApiResponseType<Array<Account>> => apiWithConfig.get("/accounts/admins"),
    getResourceManagerAccounts: (): ApiResponseType<Array<Account>> => apiWithConfig.get("/accounts/resource-managers"),
    addClientAccount: (account: Account): ApiResponseType<Account> => apiWithConfig.post("/accounts/client", account),
    addAdminAccount: (account: Account): ApiResponseType<Account> => apiWithConfig.post("/accounts/admin", account),
    addResourceManagerAccount: (account: Account): ApiResponseType<Account> => apiWithConfig.post("/accounts/resource-manager", account),
    updateClientPassword: (account: Account | undefined, login: string): ApiResponseType<Account> => apiWithConfig.put(`/accounts/client/password/${login}`, account),
    updateAdminPassword: (account: Account | undefined, login: string): ApiResponseType<Account> => apiWithConfig.put(`/accounts/admin/password/${login}`, account),
    updateResourceManagerPassword: (account: Account | undefined, login: string): ApiResponseType<Account> => apiWithConfig.put(`/accounts/resource-manager/password/${login}`, account),
    activateAccount: (login: string): ApiResponseType<Account> => apiWithConfig.patch(`/accounts/activate/${login}`),
    deactivateAccount: (login: string): ApiResponseType<Account> => apiWithConfig.patch(`/accounts/deactivate/${login}`),
    getUserRents: (userID: string | undefined): ApiResponseType<Array<RentGet>> => apiWithConfig.get(`/rents/account-id/${userID}`),
    addRent: (rent: RentPost): ApiResponseType<RentPost> => apiWithConfig.post("/rents", rent),
    endRent: (rentID: string): ApiResponseType<RentGet> => apiWithConfig.delete(`/rents/${rentID}`),
    getRooms: (): ApiResponseType<Array<Room>> => apiWithConfig.get("/rooms"),
    // post: (url: string, data: any) => apiWithConfig.post(url, data),
    // put: (url: string, data: any) => apiWithConfig.put(url, data),
    // delete: (url: string) => apiWithConfig.delete(url),
}