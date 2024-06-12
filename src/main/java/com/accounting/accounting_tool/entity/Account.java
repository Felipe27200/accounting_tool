package com.accounting.accounting_tool.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(
    name = "accounts"
)
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @Column(name = "is_recurring", nullable = true)
    private boolean isRecurring;

    @ManyToOne
    @JoinColumn(
        name = "category_id",
        referencedColumnName = "category_id",
        nullable = false
    )
    private Category category;

    @ManyToOne
    @JoinColumn(
        name = "financial_statement_id",
        referencedColumnName = "financial_statement_id",
        nullable = false
    )
    private FinancialStatement financialStatement;

    public Account() {
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(Long id, BigDecimal amount, Date date, boolean isRecurring) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.isRecurring = isRecurring;
    }

    public Account(Long id, BigDecimal amount, Date date, boolean isRecurring, Category category, FinancialStatement financialStatement) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.isRecurring = isRecurring;
        this.category = category;
        this.financialStatement = financialStatement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public FinancialStatement getFinancialStatement() {
        return financialStatement;
    }

    public void setFinancialStatement(FinancialStatement financialStatement) {
        this.financialStatement = financialStatement;
    }
}
