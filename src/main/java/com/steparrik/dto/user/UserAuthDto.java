package com.steparrik.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAuthDto {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "It is not valid email")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
