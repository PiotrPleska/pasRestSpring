import axios from 'axios'
import {ApiResponseType} from "../types/ApiResponse.ts";
import {Account, AccountLogin} from "../types/Account.ts";
import {RentGet, RentPost} from "../types/Rent.ts";
import {Room} from "../types/Room.ts";

export const API_URL = "https://localhost:8080/api"
export const TIMEOUT_IN_MS = 300000

const getAuthToken = () => 'Bearer ' + localStorage.getItem('token');

export const DEFAULT_HEADERS = {
    Accept: 'application/json',
    'Content-type': 'application/json',
    Authorization: getAuthToken(),
}


export const LOGIN_HEADERS = {
    Accept: 'application/json',
    'Content-type': 'application/json',
}

export const apiWithConfig = axios.create({
    baseURL: API_URL,
    timeout: TIMEOUT_IN_MS,
    headers: DEFAULT_HEADERS,
})

apiWithConfig.interceptors.request.use(
    (config) => {
        // Modify the request config to include the Authorization header
        config.headers.Authorization = getAuthToken();
        return config;
    },
    (error) => {
        // Handle request error
        return Promise.reject(error);
    }
);

export const apiForLogin = axios.create({
    baseURL: API_URL,
    timeout: TIMEOUT_IN_MS,
    headers: LOGIN_HEADERS,
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
    authenticate: (login: string, password: string): ApiResponseType<string> => apiWithConfig.post("/auth/authenticate", {login, password}),
    createAccount: (formData: Account): ApiResponseType<Account> => apiWithConfig.post("/auth/client", formData),
    logIn: (formData: AccountLogin): ApiResponseType<string> => apiForLogin.post("/auth/authenticate", formData),
    // post: (url: string, data: any) => apiWithConfig.post(url, data),
    // put: (url: string, data: any) => apiWithConfig.put(url, data),
    // delete: (url: string) => apiWithConfig.delete(url),
}