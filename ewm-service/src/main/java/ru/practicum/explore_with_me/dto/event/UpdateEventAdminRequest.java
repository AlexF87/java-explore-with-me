package ru.practicum.explore_with_me.dto.event;

public class UpdateEventAdminRequest {
    private String annotation;
    private long category;
    private String description;
    private String eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    //Новое состояние события
    private String stateAction;
    private String title;
}
