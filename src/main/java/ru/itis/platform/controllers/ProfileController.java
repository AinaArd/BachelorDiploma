package ru.itis.platform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itis.platform.dto.AppDto;
import ru.itis.platform.dto.EditUser;
import ru.itis.platform.dto.ResponseDto;
import ru.itis.platform.dto.UserDto;
import ru.itis.platform.models.User;
import ru.itis.platform.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/profile")
public class ProfileController {
    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserProfile(Authentication authentication, @RequestHeader("Authorization") String token) {
        Optional<User> userCandidate = userService.getCurrentUser(authentication);
        if (userCandidate.isPresent()) {
            return ResponseEntity.ok(UserDto.from(userCandidate.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("User is not found"));
        }
    }

    @PatchMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUserInfo(Authentication authentication, @RequestBody EditUser userDto) {
        User updatedUser = userService.updateUserInfo(authentication.getName(), userDto);
        return ResponseEntity.ok(EditUser.from(updatedUser));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUserPassword(Authentication authentication, @RequestBody Map<String, String> dto) {
        User updatedUser = userService.changeUserPassword(authentication.getName(), dto.get("password"));
        return ResponseEntity.ok(UserDto.from(updatedUser));
    }

    @GetMapping("/apps")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserApps(Authentication authentication) {
//        Optional<User> userCandidate = userService.getCurrentUser(authentication);
        Optional<User> userCandidate = userService.findByLogin("aina");
        if (userCandidate.isPresent()) {
            List<AppDto> appDtos = userCandidate.get().getApps().stream()
                    .map(AppDto::from)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(appDtos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("User is not found"));
        }
    }
}