package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.model.App;

public interface AppService {
    App findByApp(String name);
}
