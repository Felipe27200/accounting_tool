package com.accounting.accounting_tool.repository;

import com.accounting.accounting_tool.entity.FinancialStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FinancialStatementRepository extends JpaRepository<FinancialStatement, Long>
{
    @Query(value = "SELECT f FROM FinancialStatement f JOIN FETCH f.user u WHERE u.username = :username")
    public List<FinancialStatement> findByUsername(@Param("username") String username);
    @Query(value = "SELECT f FROM FinancialStatement f JOIN FETCH f.user u WHERE f.id = :id AND u.id = :id_user")
    public FinancialStatement findByIdAndUser(@Param("id") Long id, @Param("id_user") Long id_user);
    @Query(value = "SELECT f FROM FinancialStatement f JOIN FETCH f.user u WHERE f.name = :name AND u.id = :id_user")
    public FinancialStatement findByNameAndUser(@Param("name") String name, @Param("id_user") Long id_user);
    @Query(value = "SELECT f FROM FinancialStatement f JOIN FETCH f.user u WHERE f.name LIKE %:name% AND u.id = :id_user")
    public List<FinancialStatement> findByNameCoincidenceAndUser(@Param("name") String name, @Param("id_user") Long id_user);
}
