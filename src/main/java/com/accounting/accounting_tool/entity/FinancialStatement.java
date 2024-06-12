package com.accounting.accounting_tool.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(
    name = "financial_statements",
    uniqueConstraints = @UniqueConstraint(name = "statement_name", columnNames = "name")
)
public class FinancialStatement
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "financial_statement_id")
    private Long id;

    @Column(length = 120, nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "init_date", nullable = false)
    private Date initDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = true)
    private Date endDate;

    public FinancialStatement() {
    }

    public FinancialStatement(Long id, String name, Date initDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.initDate = initDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
