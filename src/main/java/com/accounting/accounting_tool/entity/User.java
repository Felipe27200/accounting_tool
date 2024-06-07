package com.accounting.accounting_tool.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "username", columnNames = { "username" })
    }
)
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
        name = "user_id",
        nullable = false
    )
    private Long id;

    @Column(
        nullable = false,
        length = 120
    )
    private String name;

    @Column(
        nullable = false,
        length = 120
    )
    private String username;

    @Column(
        nullable = false
    )
    private String password;

    @ManyToOne
    @JoinColumn(
        name = "role_id",
        referencedColumnName = "role_id",
        nullable = false
    )
    private Role role;

    public User() { }

    public User(Long id) {
        this.id = id;
    }

    public User(String username) {
        this.username = username;
    }

    public User(String name, String password, String username) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(Long id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
