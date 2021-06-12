package ru.itis.platform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itis.platform.dto.AppClassDto;
import ru.itis.platform.dto.AppDto;
import ru.itis.platform.dto.ClassDto;
import ru.itis.platform.services.AppService;
import ru.itis.platform.services.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/app")
public class AppController {
    private final AppService appService;

    @Autowired
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/{app-id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAppInfo(@PathVariable("app-id") Long appId) {
        List<ClassDto> classes = appService.getAppClasses(appId);
        Set<ClassDto> sortedClasses = appService.sort(classes);
        return ResponseEntity.status(HttpStatus.OK).body(sortedClasses);
    }

    @PatchMapping("/{app-id}")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateApp(@PathVariable("app-id") Long appId, @RequestBody AppClassDto appDto,
                                       Authentication authentication) {
//        User currentUser = userService.getCurrentUser(authentication)
//                .orElseThrow(() -> new IllegalArgumentException("No such user"));
        appService.setInProgress(appId);
        appService.updateApp(appDto.getWords(), appDto.getAppClassName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{app-id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> finishApp(@PathVariable("app-id") Long appId, Authentication authentication) {
        appService.finish(appId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}