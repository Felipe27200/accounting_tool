package com.accounting.accounting_tool.repository;

import com.accounting.accounting_tool.entity.AccountCatalogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountCatalogueRepository extends JpaRepository<AccountCatalogue, Long>
{
    public AccountCatalogue findByName(String name);
    public List<AccountCatalogue> findByTypeAccount(Integer typeAccount);

    @Query(
        value = "SELECT * FROM accounts_catalogue AS a WHERE name LIKE %?1%",
        nativeQuery = true
    )
    public List<AccountCatalogue> findByNameCoincidance(String name);
}
