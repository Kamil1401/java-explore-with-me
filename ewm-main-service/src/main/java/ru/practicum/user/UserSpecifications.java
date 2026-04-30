package ru.practicum.user;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class UserSpecifications {


    public static Specification<User> byIds(List<Long> ids) {
        return (root, query, cb) -> {
            if (ids == null || ids.isEmpty()) {
                return cb.conjunction(); // без фильтра
            }
            return root.get("id").in(ids);
        };
    }
}
