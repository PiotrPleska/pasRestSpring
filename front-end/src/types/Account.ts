import { AccountTypeEnum } from '../enums/AccountType.enum'
export interface Account {
    id?: string;
    login: string;
    password?: string;
    personalId: string;
    active?: boolean;
    accountType?: AccountTypeEnum;
}

export interface AccountLogin {
    login?: string;
    password?: string;
}

export interface UserWithType {
    login: string;
    accountType: AccountTypeEnum;
}