package com.joyned.reddit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddTopicRequestDto {
    @NotBlank(message = "title is a required field")
    private String title;
    @NotBlank(message = "topic description is a required field")
    private String description;
}
