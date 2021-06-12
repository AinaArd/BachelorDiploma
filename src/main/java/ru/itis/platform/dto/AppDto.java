package ru.itis.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.platform.models.App;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppDto {
    private Long id;
    private String appName;
    private String status;

    public static AppDto from(App app) {
        return AppDto.builder()
                .id(app.getId())
                .appName(app.getAppName())
                .status(String.valueOf(app.getStatus()))
                .build();
    }
}
