package com.steparrik.utils.mapper.user;

import com.steparrik.dto.user.UserRegistrationDto;
import com.steparrik.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {
    public User toEntity(UserRegistrationDto d) {
        if (d == null) {
            return null;
        }
        User user = new User();
        user.setPassword(d.getPassword());
        user.setEmail(d.getEmail());
        return user;
    }

}
