package ru.itis.platform.services;

import ru.itis.platform.dto.TokenDto;
import ru.itis.platform.dto.UserDto;

public interface UserService {
    void signup(UserDto dto);

    TokenDto login(UserDto dto);
}
