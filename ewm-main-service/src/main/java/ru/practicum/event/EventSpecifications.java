package ru.practicum.event;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class EventSpecifications {


    public static Specification<Event> published() {

        return (root, query, cb) ->
                cb.equal(root.get("state"), "PUBLISHED");
    }


    public static Specification<Event> textLike(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isBlank()) return cb.conjunction();

            String pattern = "%" + text.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("annotation")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }


    public static Specification<Event> inCategories(List<Long> categories) {
        return (root, query, cb) -> {
            if (categories == null || categories.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("category").get("id").in(categories);
        };
    }


    public static Specification<Event> paid(Boolean paid) {
        return (root, query, cb) ->
                paid == null ? cb.conjunction()
                        : cb.equal(root.get("paid"), paid);
    }


    public static Specification<Event> rangeStart(LocalDateTime rangeStart) {
        return (root, query, cb) ->
                rangeStart == null ? cb.conjunction()
                        : cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart);
    }


    public static Specification<Event> rangeEnd(LocalDateTime rangeEnd) {
        return (root, query, cb) ->
                rangeEnd == null ? cb.conjunction()
                        : cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd);
    }


    public static Specification<Event> onlyAvailable(Boolean onlyAvailable) {
        return (root, query, cb) -> {
            if (onlyAvailable == null || !onlyAvailable) {
                return cb.conjunction();
            }

            return cb.or(
                    cb.equal(root.get("participantLimit"), 0),
                    cb.lessThan(root.get("confirmedRequests"),
                            root.get("participantLimit"))
            );
        };
    }
}