package ru.practicum.explore_with_me.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.model.App;

import java.util.Optional;

public interface AppRepository  extends JpaRepository<App, Long> {
    Optional<App> findByApp(String  name);
}
