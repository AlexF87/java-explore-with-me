package ru.practicum.explore_with_me.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explore_with_me.dto.request.RequestState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class Request {

    @Id
    @Column(name = "id_req")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "id_event")
    Event event;

    @ManyToOne
    @JoinColumn(name = "id_user")
    User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RequestState status;
}
