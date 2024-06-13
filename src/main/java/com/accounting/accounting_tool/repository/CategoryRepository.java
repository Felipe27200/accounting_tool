package com.accounting.accounting_tool.repository;

import com.accounting.accounting_tool.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>
{
    public Category findByName(String name);

    @Query(
        value = """
            SELECT DISTINCT c FROM Category c 
                LEFT JOIN FETCH c.user u 
                LEFT JOIN FETCH c.accountCatalogue ac 
                LEFT JOIN FETCH u.role 
            WHERE c.name LIKE %?1%"""
    )
    public List<Category> findByNameCoincidence(String name);

    /*
    * +----------------------------------------+
    * | GET THE CHILDREN DATA AT THE SAME TIME |
    * +----------------------------------------+
    *
    * The JOIN SELECT get the children data at the same time
    * when JPA is making the query in only once, in the other hand,
    * only JOIN or INNER JOIN will get first the parent data and then
    * it'll do another query for the children's data.
    *
    * This is more useful for collections.
    * */
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.user u LEFT JOIN FETCH c.accountCatalogue ac LEFT JOIN FETCH u.role")
    List<Category> findAllWithAssociations();
}
