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
public class LoginRequestDto {
    @NotBlank(message = "Email is a required field") String email;
    @NotBlank(message = "Password is a required field") String password;
}
