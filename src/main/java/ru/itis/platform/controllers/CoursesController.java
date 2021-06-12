package ru.itis.platform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itis.platform.dto.CourseDto;
import ru.itis.platform.dto.ResponseDto;
import ru.itis.platform.models.User;
import ru.itis.platform.services.AppService;
import ru.itis.platform.services.CourseService;
import ru.itis.platform.services.UserService;

import java.util.List;
import java.util.Optional;

import static ru.itis.platform.dto.UserDto.from;

@RestController
@RequestMapping("/courses")
public class CoursesController {
    private final UserService userService;
    private final AppService appService;
    private final CourseService courseService;

    @Autowired
    public CoursesController(UserService userService, AppService appService, CourseService courseService) {
        this.userService = userService;
        this.appService = appService;
        this.courseService = courseService;
    }

    @PostMapping("/{course-id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> signUpUserForCourse(@PathVariable("course-id") Long courseId, Authentication authentication) {
        Optional<User> userCandidate = userService.getCurrentUser(authentication);
        if (userCandidate.isPresent()) {
            boolean isJoined = userService.signUpUserForCourse(userCandidate.get(), courseId);
            if (isJoined) {
                appService.createAppInstanсe(userCandidate.get(), courseId);
                return ResponseEntity.status(HttpStatus.OK).body(from(userCandidate.get()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("User has already signed up for the course."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("User is not found"));
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllCourses(Authentication authentication) {
        List<CourseDto> courses = courseService.getAllCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }
}
