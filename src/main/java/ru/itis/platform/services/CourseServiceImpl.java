package ru.itis.platform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.platform.dto.CourseDto;
import ru.itis.platform.models.App;
import ru.itis.platform.models.Course;
import ru.itis.platform.models.Status;
import ru.itis.platform.repositories.AppRepository;
import ru.itis.platform.repositories.CourseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;
    private AppRepository appRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, AppRepository appRepository) {
        this.courseRepository = courseRepository;
        this.appRepository = appRepository;
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void uploadCourse(MultipartFile file, String title, String description) {
        App newApp = App.builder()
                .appName(file.getOriginalFilename())
                .status(Status.CREATED)
                .build();
        Course newCourse = Course.builder()
                .title(title)
                .description(description)
                .build();
        courseRepository.save(newCourse);
        newApp.setCourse(newCourse);
        appRepository.save(newApp);

        newCourse.setApp(newApp);
        courseRepository.save(newCourse);
    }
}
