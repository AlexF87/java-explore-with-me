package ru.practicum.explore_with_me.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ParticipationRequestDto {
    LocalDateTime created;
    Long event;
    Long id;
    Long requester;
    RequestState status;
}
