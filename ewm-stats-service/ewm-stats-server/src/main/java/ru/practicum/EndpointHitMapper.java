package ru.practicum;

public class EndpointHitMapper {


    public static EndpointHit toEntity(EndpointHitDto dto) {
        EndpointHit hit = new EndpointHit();

        hit.setApp(dto.getApp());
        hit.setUri(dto.getUri());
        hit.setIp(dto.getIp());

        return hit;
    }

    public static ViewStats toDto(EndpointHit hit) {
        ViewStats dto = new ViewStats();

        dto.setApp(hit.getApp());
        dto.setUri(hit.getUri());

        return dto;
    }
}
