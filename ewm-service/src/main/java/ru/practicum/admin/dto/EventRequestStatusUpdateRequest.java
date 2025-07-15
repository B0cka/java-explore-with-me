package ru.practicum.admin.dto;

import lombok.*;
import ru.practicum.admin.model.RequestStatus;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {
    private Set<Long> requestIds;
    private RequestStatus status;
}