package ru.itis.platform.services;

import ru.itis.platform.dto.AppDto;
import ru.itis.platform.models.User;

public interface AppService {
    void createAppInstanсe(User user, Long courseId, AppDto appDto);
}
