package ru.practicum.explore_with_me.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
