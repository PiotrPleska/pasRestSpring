import { AccountTypeEnum } from '../enums/AccountType.enum'
export interface Account {
    id?: string;
    login: string;
    password?: string;
    personalId: string;
    active?: boolean;
    accountType?: AccountTypeEnum;
}