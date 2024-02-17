import { createContext, ReactNode, useContext, useState } from "react";
import {UserWithType} from "../types/Account.ts";

type UserContextType = {
    user: UserWithType | null;
    setUser: (user: UserWithType | null) => void;
};

const UserContext = createContext<UserContextType | undefined>(undefined);

export const useUserContext = () => {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error("useUserContext must be used within a UserContextProvider");
    }
    return context;
};

type UserContextProviderProps = {
    children: ReactNode;
};

export const UserContextProvider = ({ children }: UserContextProviderProps) => {
    const [user, setUser] = useState<UserWithType | null>(null);

    const contextValue: UserContextType = {
        user,
        setUser,
    };

    return <UserContext.Provider value={contextValue}>{children}</UserContext.Provider>;
};
