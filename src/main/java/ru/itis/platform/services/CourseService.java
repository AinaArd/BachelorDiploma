package ru.itis.platform.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.platform.dto.CourseDto;

import java.util.List;

public interface CourseService {
    List<CourseDto> getAllCourses();

    void uploadCourse(MultipartFile file, String title, String description);
}
