package ru.itis.platform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.platform.dto.ResponseDto;
import ru.itis.platform.models.User;
import ru.itis.platform.services.UserService;

import java.util.Optional;

import static ru.itis.platform.dto.UserDto.from;

@RestController
public class CoursesController {
    private final UserService userService;

    @Autowired
    public CoursesController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/courses/{course-id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> signUpUserForCourse(@PathVariable("course-id") Long courseId, Authentication authentication) {
        Optional<User> userCandidate = userService.getCurrentUser(authentication);
        if (userCandidate.isPresent()) {
            boolean isJoined = userService.signUpUserForCourse(userCandidate.get(), courseId);
            if (isJoined) {
                return ResponseEntity.status(HttpStatus.OK).body(from(userCandidate.get()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("User has already signd up for the course."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("User is not found"));
        }
    }
}