package ru.itis.platform.services;

import org.springframework.security.core.Authentication;
import ru.itis.platform.dto.EditUser;
import ru.itis.platform.dto.TokenDto;
import ru.itis.platform.dto.UserDto;
import ru.itis.platform.models.User;

import java.util.Optional;

public interface UserService {
    void signup(UserDto dto);

    TokenDto login(UserDto dto);

    Optional<User> findByLogin(String login);

    Optional<User> getCurrentUser(Authentication authentication);

    User updateUserInfo(String login, EditUser userDto);

    User changeUserPassword(String login, String password);
}
