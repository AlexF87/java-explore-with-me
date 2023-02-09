package ru.practicum.explore_with_me.dto.request;

import java.util.List;

public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    RequestState status;
}
