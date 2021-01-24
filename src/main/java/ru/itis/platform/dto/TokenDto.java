package ru.itis.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.itis.platform.models.TokenStatus;

@Data
@AllArgsConstructor
@Builder
public class TokenDto {
    private String value;
    private TokenStatus status;

    public static TokenDto empty() {
        return TokenDto.builder()
                .value("")
                .status(TokenStatus.INVALID)
                .build();
    }
}
