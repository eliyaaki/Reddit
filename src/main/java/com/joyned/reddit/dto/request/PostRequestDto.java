package com.joyned.reddit.dto.request;

import com.joyned.reddit.dto.LocationDto;
import com.joyned.reddit.dto.WeatherDataDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    @NotBlank(message = "title is a required field")
    private String title;
    @NotBlank(message = "content is a required field")
    private String content;

    private LocationDto location;
}
