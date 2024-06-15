package com.accounting.accounting_tool.repository;

import com.accounting.accounting_tool.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>
{
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
    final String BASE_SELECT_QUERY = """
            SELECT DISTINCT c FROM Category c
                LEFT JOIN FETCH c.user u
                LEFT JOIN FETCH c.accountCatalogue
                LEFT JOIN FETCH u.role""";

    @Query(BASE_SELECT_QUERY + " WHERE c.id = :categoryId AND u.id = :userId")
    Category findByIdWithAssociations(@Param("categoryId") Long categoryId, @Param("userId") Long userId);

    @Query(BASE_SELECT_QUERY + " WHERE u.id = :userId")
    List<Category> findAllWithAssociations(@Param("userId") Long userId);

    @Query(BASE_SELECT_QUERY + " WHERE c.name = :name AND u.id = :userId")
    Category findByName(@Param("name") String name, @Param("userId") Long userId);

    @Query(BASE_SELECT_QUERY + " WHERE c.name LIKE %?1% AND u.id = ?2")
    List<Category> findByNameCoincidence(String name, Long userId);

    @Query(BASE_SELECT_QUERY + " WHERE u.id = ?1")
    List<Category> findByUserId(Long id);
}
