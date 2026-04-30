package ru.practicum.compilation;

import org.springframework.data.jpa.domain.Specification;

public class CompilationSpecifications {


    public static Specification<Compilation> pinned(Boolean pinned) {
        return (root, query, cb) ->
                pinned == null ? cb.conjunction()
                        : cb.equal(root.get("pinned"), pinned);
    }
}
