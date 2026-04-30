package ru.practicum.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @Query(value = """
            SELECT *
            FROM   categories
            ORDER BY id
            LIMIT :size OFFSET :from
            """, nativeQuery = true)
    List<Category> findAll(@Param("from") int from, @Param("size") int size);
}
