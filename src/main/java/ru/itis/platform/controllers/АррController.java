package ru.itis.platform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.platform.services.UserService;

@RestController
public class АррController {
    private final UserService userService;

    @Autowired
    public АррController(UserService userService) {
        this.userService = userService;
    }

}
