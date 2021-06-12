package ru.itis.platform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.platform.dto.CourseDto;
import ru.itis.platform.dto.TokenDto;
import ru.itis.platform.dto.UserDto;
import ru.itis.platform.services.CourseService;
import ru.itis.platform.services.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private CourseService courseService;

    @Autowired
    public AdminController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<?> signInAdmin(@RequestBody UserDto dto) {
        TokenDto tokenDto = userService.login(dto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenDto.getValue());
    }

    @PreAuthorize("permitAll()")
    @PostMapping(headers = "content-type=multipart/*")
    public ResponseEntity<?> uploadCourse(@RequestParam("file") MultipartFile file,
                                          @RequestParam("title") String title,
                                          @RequestParam("description") String description) {
        courseService.uploadCourse(file, title, description);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getAllCourses());
    }
}
