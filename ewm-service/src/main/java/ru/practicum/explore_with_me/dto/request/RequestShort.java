package ru.practicum.explore_with_me.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestShort {
    private long confirmedRequests;
    private long id;

    public RequestShort(Long confirmedRequests, Long id) {
        this.confirmedRequests = confirmedRequests;
        this.id = id;
    }
}
