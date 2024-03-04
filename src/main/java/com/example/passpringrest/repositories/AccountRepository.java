package com.example.passpringrest.repositories;

import com.example.passpringrest.entities.AbstractAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AbstractAccount, UUID> {


    @Query("SELECT a FROM account a WHERE TYPE(a) = :discriminatorValue")
    List<AbstractAccount> findByDiscriminatorValue(@Param("discriminatorValue") String discriminatorValue);

    Optional<AbstractAccount> findByPersonalId(String personalId);

    Optional<AbstractAccount> findByLogin(String login);

    @Query("SELECT a FROM account a WHERE a.login LIKE %:partOfLogin%")
    List<AbstractAccount> findByPartOfLogin(@Param("partOfLogin") String partOfLogin);
}
