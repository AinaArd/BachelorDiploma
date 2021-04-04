package ru.itis.platform.services;

import ru.itis.platform.dto.AppDto;
import ru.itis.platform.models.App;
import ru.itis.platform.models.User;

public interface AppService {
    void createAppInstanсe(User user, Long courseId, AppDto appDto);

    void updateApp(AppDto appDto, User currentUser, Long appId);

    App getAppById(Long appId);
}
