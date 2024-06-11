package com.accounting.accounting_tool.repository;

import com.accounting.accounting_tool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    User findUserByUsername(String username);

    @Query(
        value = "SELECT * FROM users u WHERE u.username LIKE %?1%",
        nativeQuery = true
    )
    List<User> findUserByUsernameCoincidence(String username);
}
