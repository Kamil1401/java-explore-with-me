package ru.practicum;

import lombok.*;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class EndpointHitDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}