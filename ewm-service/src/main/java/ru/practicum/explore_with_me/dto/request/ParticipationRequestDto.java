package ru.practicum.explore_with_me.dto.request;

import java.time.LocalDateTime;

public class ParticipationRequestDto {
    LocalDateTime created;
    Long event;
    Long id;
    Long requester;
    String status;
}
