package com.accounting.accounting_tool.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(
    name = "accountings"
)
public class Accounting
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accounting_name")
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

    public Accounting() {
    }

    public Accounting(Long id) {
        this.id = id;
    }

    public Accounting(Long id, BigDecimal amount, Date date, boolean isRecurring) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.isRecurring = isRecurring;
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
}
