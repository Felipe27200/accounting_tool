package com.accounting.accounting_tool.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(
    name = "budgets"
)
public class Budget
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Long id;

    @Column(
        name = "aim",
        nullable = false
    )
    private BigDecimal aim;

    @Temporal(TemporalType.DATE)
    @Column(name = "init_date", nullable = false)
    private Date initDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @ManyToOne()
    @JoinColumn(
        name = "category_id",
        referencedColumnName = "category_id",
        nullable = false
    )
    private Category category;

    public Budget() {
    }

    public Budget(Long id, BigDecimal aim, Date initDate, Date endDate) {
        this.id = id;
        this.aim = aim;
        this.initDate = initDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAim() {
        return aim;
    }

    public void setAim(BigDecimal aim) {
        this.aim = aim;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
