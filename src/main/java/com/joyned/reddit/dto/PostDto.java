package com.joyned.reddit.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDate lastUpdated;
    private LocationDto location;
    private WeatherDataDto weather;
}
