package ru.itis.platform.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.platform.dto.ResponseDto;
import ru.itis.platform.models.User;

import java.util.Optional;

import static ru.itis.platform.dto.UserDto.from;

@RestController("/cources")
public class CoursesController {
//    @PostMapping
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<?> assignUserToCourse(Authentication authentication) {
//        Optional<User> userCandidate = userService.getCurrentUser(authentication);
//        if (userCandidate.isPresent()) {
//
//
//            return ResponseEntity.ok(from(userCandidate.get()));
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("User is not found"));
//        }
//    }

}
