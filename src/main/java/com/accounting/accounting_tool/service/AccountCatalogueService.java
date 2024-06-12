package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.entity.AccountCatalogue;
import com.accounting.accounting_tool.error_handling.exception.DuplicateRecordException;
import com.accounting.accounting_tool.error_handling.exception.NotFoundException;
import com.accounting.accounting_tool.repository.AccountCatalogueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountCatalogueService
{
    private AccountCatalogueRepository accountCatalogueRepository;

    @Autowired
    public AccountCatalogueService(AccountCatalogueRepository accountCatalogueRepository)
    {
        this.accountCatalogueRepository = accountCatalogueRepository;
    }

    @Transactional
    public AccountCatalogue save(AccountCatalogue newAccountCatalogue)
    {
        this.checkUniqueName(newAccountCatalogue.getName());

        return this.accountCatalogueRepository.save(newAccountCatalogue);
    }

    @Transactional
    public AccountCatalogue update(AccountCatalogue accountCatalogue)
    {
        AccountCatalogue oldAccountCatalogue = this.findById(accountCatalogue.getId());
        AccountCatalogue checkNotDuplicate = this.accountCatalogueRepository.findByName(accountCatalogue.getName());

        if (checkNotDuplicate != null
            && (!checkNotDuplicate.getId().equals(accountCatalogue.getId())
            && checkNotDuplicate.getName().equals(accountCatalogue.getName())))
        {
            throw new DuplicateRecordException(String.format(
                "The account catalogue with the name %s belongs to another Account catalogue.",
                accountCatalogue.getName()));
        }

        oldAccountCatalogue.setName(accountCatalogue.getName());
        oldAccountCatalogue.setTypeAccount(accountCatalogue.getTypeAccount());

        return this.accountCatalogueRepository.save(oldAccountCatalogue);
    }

    public List<AccountCatalogue> findAll()
    {
        return this.accountCatalogueRepository.findAll();
    }

    public AccountCatalogue findById(Long id)
    {
        Optional<AccountCatalogue> accountCatalogue = this.accountCatalogueRepository.findById(id);

        if (accountCatalogue.isEmpty() || !(accountCatalogue.isPresent()))
            throw new NotFoundException("The Account Catalogue with the id '" + id + "' was not found.");

        return accountCatalogue.get();
    }

    @Transactional
    public String deleteById (Long id)
    {
        AccountCatalogue accountCatalogue = this.findById(id);
        this.accountCatalogueRepository.deleteById(accountCatalogue.getId());

        return "The Account Catalogue with the name: '" + accountCatalogue.getName() + "' was deleted.";
    }

    private void checkUniqueName(String name)
    {
        AccountCatalogue accountCatalogue = this.accountCatalogueRepository.findByName(name);

        if (accountCatalogue != null)
            throw new DuplicateRecordException("The account catalogue with the name '" + name + "' already exists");
    }
}
