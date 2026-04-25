package ru.practicum.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Optional<Compilation> findByTitle(String title);

    @Query(value = """
            SELECT    *
            FROM      compilations
            WHERE     pinned = :pinned
            ORDER BY  id
            LIMIT :size OFFSET :from
            """, nativeQuery = true)
    List<Compilation> findAll(@Param("pinned") Boolean pinned, @Param("from") int from, @Param("size") int size);
}
