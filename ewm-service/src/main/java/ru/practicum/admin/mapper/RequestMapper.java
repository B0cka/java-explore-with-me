package ru.practicum.admin.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.admin.dto.ParticipationRequestDto;
import ru.practicum.admin.model.Request;

import java.time.temporal.ChronoUnit;

@UtilityClass
public class RequestMapper {

    public ParticipationRequestDto toDto(Request request) {

        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .requester(request.getRequester().getId())
                .status(request.getStatus().toString())
                .build();


    }

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .created(request.getCreated().truncatedTo(ChronoUnit.MICROS))
                .requester(request.getRequester().getId())
                .status(request.getStatus().toString())
                .build();
    }

}
