package com.accounting.accounting_tool.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "categories",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "category_name",
            columnNames = { "name", "user_id" }
        )
    }
)
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(
        length = 120,
        nullable = false
    )
    private String name;

    @Column(
        name = "parent_category",
        nullable = true
    )
    private Long parentCategory;

    @ManyToOne()
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "user_id",
        nullable = false
    )
    private User user;

    @ManyToOne()
    @JoinColumn(
        name = "account_catalogue_id",
        referencedColumnName = "account_catalogue_id",
        nullable = false
    )
    private AccountCatalogue catalogue;

    public Category() { }

    public Category(Long id) {
        this.id = id;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Category(Long id, String name, Long parentCategory) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
    }

    public Category(Long id, String name, Long parentCategory, User user, AccountCatalogue catalogue) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
        this.user = user;
        this.catalogue = catalogue;
    }

    public Category(Long id, String name, Long parentCategory, User user) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
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

    public Long getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Long parentCategory) {
        this.parentCategory = parentCategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AccountCatalogue getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(AccountCatalogue catalogue) {
        this.catalogue = catalogue;
    }
}
