package com.example.passpringrest.converters;


import com.example.passpringrest.dto.AbstractAccountDto;
import com.example.passpringrest.dto.AdminAccountDto;
import com.example.passpringrest.dto.ClientAccountDto;
import com.example.passpringrest.dto.ResourceManagerAccountDto;
import com.example.passpringrest.entities.AbstractAccount;
import com.example.passpringrest.entities.AdminAccount;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.entities.ResourceManagerAccount;

public class AccountConverter {

    public static ClientAccountDto toClientDto(ClientAccount account) {
        return new ClientAccountDto(account.getId(), account.getLogin(), account.getPersonalId(), account.getActive());
    }

    public static ClientAccount toClient(ClientAccountDto account) {
        return new ClientAccount(account.getId(), account.getLogin(), account.getPassword(), account.getPersonalId(),
                account.getActive());
    }

    public static AdminAccountDto toAdminDto(AdminAccount account) {
        return new AdminAccountDto(account.getId(), account.getLogin(), account.getPersonalId(), account.getActive());
    }

    public static AdminAccount toAdmin(AdminAccountDto account) {
        return new AdminAccount(account.getId(), account.getLogin(), account.getPassword(), account.getPersonalId(),
                account.getActive());
    }

    public static ResourceManagerAccount toResourceManager(ResourceManagerAccountDto account) {
        return new ResourceManagerAccount(account.getId(), account.getLogin(), account.getPassword(), account.getPersonalId(),
                account.getActive());
    }

    public static ResourceManagerAccountDto toResourceManagerDto(ResourceManagerAccount account) {
        return new ResourceManagerAccountDto(account.getId(), account.getLogin(), account.getPersonalId(), account.getActive());
    }

    public static AbstractAccountDto solveAccountDtoType(AbstractAccount account) {
        AbstractAccountDto accountDto = null;
        if (account instanceof AdminAccount)
            accountDto = AccountConverter.toAdminDto((AdminAccount) account);
        else if (account instanceof ResourceManagerAccount)
            accountDto = AccountConverter.toResourceManagerDto((ResourceManagerAccount) account);
        else if (account instanceof ClientAccount)
            accountDto = AccountConverter.toClientDto((ClientAccount) account);
        return accountDto;
    }

    public static AbstractAccount solveAccountEntityType(AbstractAccountDto accountDto) {
        AbstractAccount account = null;
        if (accountDto instanceof AdminAccountDto)
            account = AccountConverter.toAdmin((AdminAccountDto) accountDto);
        else if (accountDto instanceof ResourceManagerAccountDto)
            account = AccountConverter.toResourceManager((ResourceManagerAccountDto) accountDto);
        else if (accountDto instanceof ClientAccountDto)
            account = AccountConverter.toClient((ClientAccountDto) accountDto);
        return account;
    }
}
