package ru.itis.platform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.platform.dto.ResponseDto;
import ru.itis.platform.dto.UserDto;
import ru.itis.platform.services.UserService;

@RestController
public class SingupController {
    private final UserService userService;

    @Autowired
    public SingupController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/signup")
    public ResponseEntity<?> signUpNewUser(@RequestBody UserDto dto) {
        userService.signup(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Successfully added user"));
    }
}
