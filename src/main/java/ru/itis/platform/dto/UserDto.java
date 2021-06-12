package ru.itis.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.platform.models.Role;
import ru.itis.platform.models.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String login;
    private String password;
    private String fullName;
    private List<CourseDto> courses;
    private Role role;

    public static UserDto from(User user) {
        return UserDto.builder()
                .login(user.getLogin())
                .fullName(user.getFullName())
                .courses(user.getCourses()
                        .stream()
                        .map(CourseDto::from)
                        .collect(Collectors.toList()))
                .role(user.getRole())
                .build();
    }
}
