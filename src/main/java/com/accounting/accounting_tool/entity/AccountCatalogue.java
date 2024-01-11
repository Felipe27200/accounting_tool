package com.accounting.accounting_tool.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "accounts_catalogue",
    uniqueConstraints = @UniqueConstraint(name = "catalogue_name", columnNames = "name")
)
public class AccountCatalogue
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_catalogue_id")
    private Long id;

    @Column(
        name = "name",
        length = 70,
        nullable = false
    )
    private String name;

    @Column(
        name = "is_earning",
        nullable = false
    )
    private boolean isEarning;

    public AccountCatalogue() { }

    public AccountCatalogue(Long id) {
        this.id = id;
    }

    public AccountCatalogue(String name) {
        this.name = name;
    }

    public AccountCatalogue(Long id, String name, boolean isEarning) {
        this.id = id;
        this.name = name;
        this.isEarning = isEarning;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isEarning() {
        return isEarning;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEarning(boolean earning) {
        isEarning = earning;
    }
}
