package com.joyned.reddit.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "First-name is a required field")
    private String firstName;
    @NotBlank(message = "last-name is a required field")
    private String lastName;
    @NotBlank(message = "email is a required field")
    @Column(unique = true)
    private String email;

    private LocalDate lastConnectivity;
}
