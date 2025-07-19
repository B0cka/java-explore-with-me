package ru.practicum.admin.dto;

import java.util.Optional;

public enum EventUserState {
    SEND_TO_REVIEW, CANCEL_REVIEW, PUBLISH_EVENT, REJECT_EVENT;

    public static Optional<EventUserState> from(String stringState) {
        for (EventUserState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}