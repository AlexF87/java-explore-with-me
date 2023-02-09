package ru.practicum.explore_with_me.dto.event;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Location {
    private Float lat;
    private Float lon;
}
