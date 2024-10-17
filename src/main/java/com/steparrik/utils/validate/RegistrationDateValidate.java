package com.steparrik.utils.validate;

import com.steparrik.dto.user.UserRegistrationDto;
import com.steparrik.service.user.UserService;
import com.steparrik.utils.exception.ApiException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationDateValidate {
    private final UserService userService;

    public void validateRegistrationDate(UserRegistrationDto userRegistrationDto) {
        if (!checkConfirmPassword(userRegistrationDto.getPassword(), userRegistrationDto.getConfirmPassword())) {
            throw new ApiException("Passwords don't match", HttpStatus.BAD_REQUEST);
        }
        if (!checkUserEmail(userRegistrationDto.getEmail())) {
            throw new ApiException("User with this email is already registered", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkUserEmail(String email) {
        try {
            userService.findByEmail(email);
            return false;
        } catch (ApiException e) {
            return true;
        }
    }

    private boolean checkConfirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

}