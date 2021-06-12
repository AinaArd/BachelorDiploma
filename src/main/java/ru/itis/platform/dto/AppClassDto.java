package ru.itis.platform.dto;

import lombok.Data;

import java.util.List;

@Data
public class AppClassDto {
    private String appClassName;
    private String code;
    private List<String> words;
}
