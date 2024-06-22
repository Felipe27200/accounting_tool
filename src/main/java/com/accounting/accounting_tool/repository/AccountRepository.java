package com.accounting.accounting_tool.repository;

import com.accounting.accounting_tool.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accounting.accounting_tool.dto.account.SelectAccountDTO;
import com.accounting.accounting_tool.dto.category.CategoryForAccountDTO;
import com.accounting.accounting_tool.dto.financial_statement.FinancialStatementForAccountDTO;
import com.accounting.accounting_tool.dto.user.GetUserDTO;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>
{
/*    final String BASES_SELECT = """
        SELECT DISTINCT a.id, a.amount, a.date,
            c.id, c.name, f.name,
            f.initDate, f.endDate, c.user
            LEFT JOIN FETCH a.category c
            LEFT JOIN FETCH a.financialStatement f
            LEFT JOIN FETCH c.user u
            LEFT JOIN FETCH u.role""";*/

    // @Query(BASES_SELECT + " WHERE a.id = :accountId AND u.id = :userId")
    @Query("""
        SELECT new com.accounting.accounting_tool.dto.account.SelectAccountDTO(
            a.id, a.amount, a.date, a.isRecurring, f.initDate 
            new com.accounting.accounting_tool.dto.category.CategoryForAccountDTO(c.id, c.parentCategory, c.name, c.accountCatalogue)
            new com.accounting.accounting_tool.dto.financial_statement.FinancialStatementForAccountDTO(f.id, f.name, f.initDate, f.endDate)
            new com.accounting.accounting_tool.dto.user.GetUserDTO(u.id, u.name, u.username)
        )
        FROM Account a
            LEFT JOIN a.category c
            LEFT JOIN a.financialStatement f
            LEFT JOIN c.user u
            LEFT JOIN u.role r
        WHERE a.id = :accountId AND u.id = :userId""")
    Account findAccountByIdAndUser(@Param("accountId") Long id, @Param("userId") Long userId);
}
