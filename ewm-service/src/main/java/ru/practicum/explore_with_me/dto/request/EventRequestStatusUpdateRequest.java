package ru.practicum.explore_with_me.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestState status;
}
