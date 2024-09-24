package com.accounting.accounting_tool.repository.custom;

import com.accounting.accounting_tool.dto.account.FilterAccountDTO;
import com.accounting.accounting_tool.dto.account.SelectAccountDTO;

import java.util.List;

/*
* Interface that let us create a custom repository
* handle complex queries.
* */
public interface CustomizedAccountRepository
{
    public List<SelectAccountDTO> filterAccounts(FilterAccountDTO filterAccountDTO, String username);
}
