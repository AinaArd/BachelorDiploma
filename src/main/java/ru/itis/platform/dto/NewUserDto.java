package ru.itis.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.platform.models.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    private String login;
    private String password;
    private String fullName;
    private Role role;
}
