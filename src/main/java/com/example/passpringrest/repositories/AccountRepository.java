package com.example.passpringrest.repositories;

import com.example.passpringrest.codecs.MongoUUID;
import com.example.passpringrest.entities.AbstractAccount;
import com.example.passpringrest.entities.AdminAccount;
import com.example.passpringrest.entities.ClientAccount;
import com.example.passpringrest.entities.ResourceManagerAccount;
import com.example.passpringrest.exceptions.ResourceNotFoundException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepository extends AbstractMongoRepository {

    private MongoCollection<AbstractAccount> accountCollection;

    public AccountRepository() {
        this.accountCollection = initDbConnection().getCollection("accounts", AbstractAccount.class);
        Document index = new Document();
        index.put("login", 1);
        IndexOptions indexOptions = new IndexOptions().unique(true);
        accountCollection.createIndex(index, indexOptions);
        Document index2 = new Document();
        index2.put("personalId", 1);
        accountCollection.createIndex(index2, indexOptions);
    }

    public void insertAccount(AbstractAccount abstractAccount) {
        accountCollection.insertOne(abstractAccount);
    }

    public ArrayList<AbstractAccount> readAllAccounts() {
        ArrayList<AbstractAccount> adminAccounts = readAdminAccounts();
        ArrayList<AbstractAccount> resourceManagerAccounts = readResourceManagerAccounts();
        ArrayList<AbstractAccount> clientAccounts = readClientAccounts();
        ArrayList<AbstractAccount> allAccounts = new ArrayList<>();
        allAccounts.addAll(adminAccounts);
        allAccounts.addAll(resourceManagerAccounts);
        allAccounts.addAll(clientAccounts);
        return allAccounts;
    }

    public ArrayList<AbstractAccount> readAdminAccounts() {
        MongoCollection<AdminAccount> adminAccountsCollection = initDbConnection().getCollection("accounts", AdminAccount.class);
        Bson filter = Filters.eq("_clazz", "AdminAccount");
        return adminAccountsCollection.find(filter).into(new ArrayList<>());
    }

    public ArrayList<AbstractAccount> readResourceManagerAccounts() {
        MongoCollection<ResourceManagerAccount> resourceManagerAccountsCollection = initDbConnection().getCollection("accounts",
                ResourceManagerAccount.class);
        Bson filter = Filters.eq("_clazz", "ResourceManagerAccount");
        return resourceManagerAccountsCollection.find(filter).into(new ArrayList<>());
    }

    public ArrayList<AbstractAccount> readClientAccounts() {
        MongoCollection<ClientAccount> clientAccountsCollection = initDbConnection().getCollection("accounts", ClientAccount.class);
        Bson filter = Filters.eq("_clazz", "ClientAccount");
        return clientAccountsCollection.find(filter).into(new ArrayList<>());
    }

    public AbstractAccount readAccountById(MongoUUID id) throws ResourceNotFoundException {
        try {
            Bson filter = Filters.eq("_id", id);
            return accountCollection.find(filter).first();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Account with id " + id + " not found");
        }
    }

    public AbstractAccount readAccountByPersonalId(String personalId) throws ResourceNotFoundException {
        try {
            Bson filter = Filters.eq("personalId", personalId);
            AbstractAccount account = accountCollection.find(filter).first();
            if (account == null) {
                throw new ResourceNotFoundException("Account with personal id " + personalId + " not found");
            }
            return account;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Account with personal id " + personalId + " not found");
        }
    }

    public AbstractAccount readAccountByLogin(String login) throws ResourceNotFoundException {
        try {
            Bson filter = Filters.eq("login", login);
            AbstractAccount account = accountCollection.find(filter).first();
            if (account == null) {
                throw new ResourceNotFoundException("Account with login " + login + " not found");
            }
            return account;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Account with login " + login + " not found");
        }
    }

    public List<AbstractAccount> readAccountsByPartOfLogin(String partOfLogin) throws ResourceNotFoundException {
        try {
            Bson filter = Filters.regex("login", partOfLogin);
            List<AbstractAccount> accounts = accountCollection.find(filter).into(new ArrayList<>());
            if (accounts.isEmpty()) {
                throw new ResourceNotFoundException("Account with login " + partOfLogin + " not found");
            }
            return accounts;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Account with login " + partOfLogin + " not found");
        }
    }

    public AbstractAccount readAccountByLoginWithoutException(String login) {
        Bson filter = Filters.eq("login", login);
        return accountCollection.find(filter).first();
    }

    public AbstractAccount readAccountByPersonalIdWithoutException(String personalId) {
        Bson filter = Filters.eq("personalId", personalId);
        return accountCollection.find(filter).first();
    }

    public void updateAccountActiveByLogin(String login, boolean active) throws ResourceNotFoundException {
        try {
            readAccountByLogin(login);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Account with login " + login + " not found");
        }
        Bson filter = Filters.eq("login", login);
        Bson setUpdate = Updates.set("active", active);
        accountCollection.updateOne(filter, setUpdate);
    }

    public void updateAccountPasswordByLogin(String login, String password) throws ResourceNotFoundException {
        try {
            readAccountByLogin(login);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Account with login " + login + " not found");
        }
        Bson filter = Filters.eq("login", login);
        Bson setUpdate = Updates.set("password", password);
        accountCollection.updateOne(filter, setUpdate);
    }


    public void dropAccountCollection() {
        accountCollection.drop();
        // I know that this is duplicated code but don't touch it
        accountCollection = initDbConnection().getCollection("accounts", AbstractAccount.class);
        Document index = new Document();
        index.put("login", 1);
        IndexOptions indexOptions = new IndexOptions().unique(true);
        accountCollection.createIndex(index, indexOptions);
        Document index2 = new Document();
        index2.put("personalId", 1);
        accountCollection.createIndex(index2, indexOptions);
    }

}
