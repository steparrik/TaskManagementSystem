package com.steparrik.utils.mapper.user;

import com.steparrik.dto.user.UserResponseDto;
import com.steparrik.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {

    public UserResponseDto toDto(User e) {
        if (e == null) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(e.getEmail());
        return userResponseDto;
    }
}
