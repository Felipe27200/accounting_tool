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
        name = "type_account",
        nullable = false,
        columnDefinition = "TINYINT"
    )
    private Integer typeAccount;

    public AccountCatalogue() { }

    public AccountCatalogue(Long id) {
        this.id = id;
    }

    public AccountCatalogue(String name) {
        this.name = name;
    }

    public AccountCatalogue(Long id, String name, Integer typeAccount) {
        this.id = id;
        this.name = name;
        this.typeAccount = typeAccount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer typeAccount() {
        return typeAccount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(Integer typeAccount) {
        this.typeAccount = typeAccount;
    }
}
