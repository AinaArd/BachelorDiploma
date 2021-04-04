package ru.itis.platform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.platform.dto.AppDto;
import ru.itis.platform.models.App;
import ru.itis.platform.models.User;
import ru.itis.platform.repositories.AppRepository;
import ru.itis.platform.repositories.CourseRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AppServiceImpl implements AppService {
    private final CourseRepository courseRepository;
    private final AppRepository appRepository;

    @Autowired
    public AppServiceImpl(CourseRepository courseRepository, AppRepository appRepository) {
        this.courseRepository = courseRepository;
        this.appRepository = appRepository;
    }

    @Override
    public void createAppInstanсe(User user, Long courseId, AppDto appDto) {
        App newApp = App.builder()
                .appName(appDto.getAppName())
                .creationDate(LocalDateTime.of(LocalDate.now(), LocalTime.now()))
                .user(user)
                .build();
        appRepository.save(newApp);
//        TODO: bind with course
    }

    @Override
    public void updateApp(AppDto appDto, User currentUser, Long appId) {
        App app = getAppById(appId);
        if(appDto.getCode() != null) {
            app.setCode(appDto.getCode());
        }
        appRepository.save(app);
    }

    @Override
    public App getAppById(Long appId) {
        return appRepository.findById(appId).orElseThrow(() -> new IllegalArgumentException("No app with such id"));
    }
}
