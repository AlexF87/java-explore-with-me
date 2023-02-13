package ru.practicum.explore_with_me.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compilations")
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "events_compilations",
            joinColumns = @JoinColumn(name = "id_comp"),
            inverseJoinColumns = @JoinColumn(name = "id_event"))
    Set<Event> events;

    @Column(nullable = false)
    Boolean pinned = false;

    @Column(nullable = false)
    String title;

    public Compilation(Set<Event> events, Boolean pinned, String title) {
        this.events = events;
        this.pinned = pinned;
        this.title = title;
    }
}
