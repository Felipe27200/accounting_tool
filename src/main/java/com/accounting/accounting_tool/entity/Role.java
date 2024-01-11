package com.accounting.accounting_tool.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "roles",
    uniqueConstraints = @UniqueConstraint(name = "role_name", columnNames = "name")
)
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(
        length = 75,
        nullable = false
    )
    private String name;
}
