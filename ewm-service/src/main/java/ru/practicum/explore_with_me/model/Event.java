package ru.practicum.explore_with_me.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.dto.event.EventState;
import ru.practicum.explore_with_me.dto.event.Location;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cat", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests", nullable = false)
    private Long confirmedRequests;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_initiator", nullable = false)
    private User initiator;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventState state;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Long views;
}
