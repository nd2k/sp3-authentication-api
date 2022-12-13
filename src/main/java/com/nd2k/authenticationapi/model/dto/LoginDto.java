package com.nd2k.authenticationapi.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {

    @NotBlank(message = "Email is a mandatory field")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Password is a mandatory field")
    @Size(min = 6, max = 128, message = "The field must contain between 6 and 128 characters")
    private String password;
}
