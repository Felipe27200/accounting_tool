package com.accounting.accounting_tool.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(
    name = "financial_statements"
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

    @ManyToOne()
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "user_id",
        nullable = false
    )
    private User user;

    public FinancialStatement() {
    }

    public FinancialStatement(Long id, String name, Date initDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.initDate = initDate;
        this.endDate = endDate;
    }

    public FinancialStatement(Long id, String name, Date initDate, Date endDate, User user) {
        this.id = id;
        this.name = name;
        this.initDate = initDate;
        this.endDate = endDate;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
