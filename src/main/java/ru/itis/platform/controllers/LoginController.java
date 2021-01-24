package ru.itis.platform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.platform.dto.TokenDto;
import ru.itis.platform.dto.UserDto;
import ru.itis.platform.services.UserService;

@RestController
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<?> singinUser(@RequestBody UserDto dto) {
        TokenDto tokenDto = userService.login(dto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenDto.getValue());
    }
}
