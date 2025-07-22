package ru.practicum.admin.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.admin.dto.EventFullDto;
import ru.practicum.admin.dto.EventShortDto;
import ru.practicum.admin.dto.NewEventDto;
import ru.practicum.admin.model.CommentStatus;
import ru.practicum.admin.model.Event;

import java.util.List;

@UtilityClass
public class EventMapper {

    public Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .id(null)
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(LocationMapper.toLocation(newEventDto.getLocation()))
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public EventFullDto toEventFullDtoForAdmin(Event event, int confirmedRequests, long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedDate())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getEventStatus())
                .title(event.getTitle())
                .confirmedRequests(confirmedRequests)
                .views(views)
                .comments(
                        event.getComments() == null ? List.of() :
                                event.getComments().stream()
                                        .filter(c -> c.getStatus() == CommentStatus.APPROVED)
                                        .map(CommentMapper::toFullDto)
                                        .toList()
                )
                .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .comments(
                        event.getComments() == null ? List.of() :
                                event.getComments().stream()
                                        .filter(c -> c.getStatus() == CommentStatus.APPROVED)
                                        .map(CommentMapper::toFullDto)
                                        .toList()
                )
                .title(event.getTitle())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFull = EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedDate())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getEventStatus())
                .title(event.getTitle())
                .confirmedRequests(0)
                .views(0L) // изменяется в дальнейшем
                .comments(
                        event.getComments() == null ? List.of() :
                                event.getComments().stream()
                                        .filter(c -> c.getStatus() == CommentStatus.APPROVED)
                                        .map(CommentMapper::toFullDto)
                                        .toList()
                )
                .build();

        return eventFull;
    }
}
