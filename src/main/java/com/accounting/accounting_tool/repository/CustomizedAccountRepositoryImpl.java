package com.accounting.accounting_tool.repository;

import com.accounting.accounting_tool.dto.account.FilterAccountDTO;
import com.accounting.accounting_tool.dto.account.SelectAccountDTO;
import com.accounting.accounting_tool.dto.category.CategoryForAccountDTO;
import com.accounting.accounting_tool.dto.financial_statement.FinancialStatementForAccountDTO;
import com.accounting.accounting_tool.repository.custom.CustomizedAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CustomRepository interface
 * that makes the repository available to the app and
 * define the necessary logic.
 * */
@Repository
public class CustomizedAccountRepositoryImpl implements CustomizedAccountRepository
{
    private final String FILTER_ACCOUNT_QUERY = """
        SELECT
            a.account_id as account_id, a.amount as amount,  a.date as date, a.is_recurring as is_recurring,
            c.category_id as category_id, c.parent_category as parent_category, c.name as category_name, c.account_catalogue_id as catalogue_id,
            f.financial_statement_id as statement_id, f.name as statement_name, f.init_date as init_date, f.end_date as end_date,
            u.user_id as user_id, u.name as user_name, u.username as username, u.role_id as role_id
        FROM accounting_system.accounts a
            INNER JOIN accounting_system.financial_statements f ON a.financial_statement_id = f.financial_statement_id
            INNER JOIN accounting_system.categories c ON c.category_id = a.category_id
            INNER JOIN accounting_system.users u ON u.user_id = c.user_id\n""";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomizedAccountRepositoryImpl(JdbcTemplate jdbcTemplate)
    {
        /**
         * JDBC implementation by spring boot for default
         * */
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SelectAccountDTO> filterAccounts(FilterAccountDTO filterAccountDTO, String username)
    {
    	ArrayList<String> parameters = new ArrayList<>();
    	
        String conditions = "WHERE 1=1\n";

        if (filterAccountDTO.getCategoryId() != null && filterAccountDTO.getCategoryId() > 0)
        {
        	conditions += "AND c.category_id = ? \n";
        	parameters.add(filterAccountDTO.getCategoryId().toString());
        }
        
        conditions += "AND u.username = ?\n";
        parameters.add(username);
        
        System.out.println("\n\n---------------------------------------------------------------------\n");
        System.out.println(parameters.size());
        System.out.println(FILTER_ACCOUNT_QUERY + conditions);

        // This is the way to run a script
        List<SelectAccountDTO> accounts = this.jdbcTemplate.query((FILTER_ACCOUNT_QUERY + conditions),
            (rs, rowNum) ->
                new SelectAccountDTO
                (
                    rs.getLong("account_id"),
                    rs.getBigDecimal("amount"),
                    rs.getDate("date"),
                    rs.getBoolean("is_recurring"),
                    new CategoryForAccountDTO
                    (
                        rs.getLong("category_id"),
                        rs.getLong("parent_category"),
                        rs.getString("category_name")
                    ),
                    new FinancialStatementForAccountDTO
                    (
                        rs.getLong("statement_id"),
                        rs.getString("statement_name"),
                        rs.getDate("init_date"),
                        rs.getDate("end_date")
                    )
        		),
            parameters
        );
        
        return accounts;
    }
}
