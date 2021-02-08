package ru.itis.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.platform.models.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUser {
    private String fullName;

    public static EditUser from(User user) {
        return EditUser.builder()
                .fullName(user.getFullName())
                .build();
    }
}
