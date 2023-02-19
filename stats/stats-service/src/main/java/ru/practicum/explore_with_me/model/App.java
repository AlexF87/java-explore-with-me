package ru.practicum.explore_with_me.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "apps")
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_app", nullable = false)
    private long id;

    @Column(nullable = false)
    private String app;

    public App(String app) {
        this.app = app;
    }
}
