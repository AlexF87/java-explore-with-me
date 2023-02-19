package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.model.App;
import ru.practicum.explore_with_me.repository.AppRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    private final AppRepository appRepository;

    @Override
    public App findByApp(String nameApp) {
        Optional<App> app = appRepository.findByApp(nameApp);
        if (app.isEmpty()) {
           return appRepository.save(new App(nameApp));
        }
        return app.get();
    }
}
