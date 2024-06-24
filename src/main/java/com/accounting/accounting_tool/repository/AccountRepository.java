package com.accounting.accounting_tool.repository;

import com.accounting.accounting_tool.dto.account.SelectAccountDTO;
import com.accounting.accounting_tool.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>
{
    /*
    * +------------------------+
    * | CONSTRUCTOR EXPRESSION |
    * +------------------------+
    *
    * It is the new with the route of an Entity or DTO.
    * It is used to set up how the object will instantiate.
    * */
    final String BASES_SELECT = """
        SELECT new com.accounting.accounting_tool.dto.account.SelectAccountDTO(
            a.id, a.amount, a.date, a.isRecurring,
            new com.accounting.accounting_tool.dto.category.CategoryForAccountDTO(c.id, c.parentCategory, c.name, c.accountCatalogue),
            new com.accounting.accounting_tool.dto.financial_statement.FinancialStatementForAccountDTO(f.id, f.name, f.initDate, f.endDate),
            new com.accounting.accounting_tool.dto.user.GetUserDTO(u.id, u.name, u.username, u.role)
        )
        FROM Account a
            LEFT JOIN a.category c
            LEFT JOIN a.financialStatement f
            LEFT JOIN c.user u
            LEFT JOIN u.role r\n""";

    @Query(BASES_SELECT + "WHERE a.id = :accountId")
    SelectAccountDTO findAccountById(@Param("accountId") Long accountId);

    @Query(BASES_SELECT + "WHERE a.id = :accountId AND u.id = :userId")
    SelectAccountDTO findAccountByIdAndUser(@Param("accountId") Long accountId, @Param("userId") Long userId);

    @Query(BASES_SELECT + "WHERE u.id = :userId ")
    List<SelectAccountDTO> findAllByUser(@Param("userId") Long userId);

    @Query(BASES_SELECT + "WHERE a.date = :date AND u.id = :userId")
    List<SelectAccountDTO> findByDateAndUser(@Param("date") String date, @Param("userId") Long userId);

    @Query(BASES_SELECT + "WHERE a.date = :date AND u.id = :userId")
    List<SelectAccountDTO> findByDateAndUser(@Param("date") Date date, @Param("userId") Long userId);

    @Query(BASES_SELECT + "WHERE (a.date BETWEEN :startDate AND :endDate) AND u.id = :userId")
    List<SelectAccountDTO> findByDatRageAndUser(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("userId") Long userId
    );

    @Query(BASES_SELECT + "WHERE (a.date BETWEEN :startDate AND :endDate) AND u.id = :userId")
    List<SelectAccountDTO> findByDatRageAndUser(
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("userId") Long userId
    );
}
