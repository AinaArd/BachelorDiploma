package ru.itis.platform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itis.platform.dto.AppDto;
import ru.itis.platform.models.App;
import ru.itis.platform.models.User;
import ru.itis.platform.services.AppService;
import ru.itis.platform.services.UserService;

@RestController
@RequestMapping("/app")
public class AppController {
    private final UserService userService;
    private final AppService appService;

    @Autowired
    public AppController(UserService userService, AppService appService) {
        this.userService = userService;
        this.appService = appService;
    }

    @GetMapping("/{app-id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAppInfo(@PathVariable("app-id") Long appId) {
        App app = appService.getAppById(appId);
        return ResponseEntity.status(HttpStatus.OK).body(app);
    }

    @PatchMapping("/{app-id}")
    public ResponseEntity<?> saveApp(@PathVariable("app-id") Long appId, @RequestBody AppDto appDto, Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication)
                .orElseThrow(() -> new IllegalArgumentException("No such user"));
        appService.updateApp(appDto, currentUser, appId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
