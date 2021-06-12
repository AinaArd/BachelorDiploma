package ru.itis.platform.services;

import ru.itis.platform.dto.AppDto;
import ru.itis.platform.dto.ClassDto;
import ru.itis.platform.models.App;
import ru.itis.platform.models.User;

import java.util.List;
import java.util.Set;

public interface AppService {

    void createAppInstanсe(User user, Long courseId);

    void updateApp(List<String> words, String className);

    App getAppById(Long appId);

    void finish(Long appId);

    List<ClassDto> getAppClasses(Long appId);

    Set<ClassDto> sort(List<ClassDto> classes);

    void setInProgress(Long appId);
}