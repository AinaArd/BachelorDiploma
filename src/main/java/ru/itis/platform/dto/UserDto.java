package ru.itis.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.platform.models.Course;
import ru.itis.platform.models.Role;
import ru.itis.platform.models.User;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String login;
    private String password;
    private String fullName;
    private List<Course> courses;
    private Role role;

    public static UserDto from(User user) {
        return UserDto.builder()
                .login(user.getLogin())
                .courses(user.getCourses())
                .role(user.getRole())
                .build();
    }
}
