package ru.practicum.event;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Location {

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lon;
}