package com.steparrik.service.user;

import com.steparrik.dto.user.UserRegistrationDto;
import com.steparrik.entity.User;
import com.steparrik.utils.mapper.user.UserRegistrationMapper;
import com.steparrik.utils.validate.RegistrationDateValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserService userService;
    private final UserRegistrationMapper userRegistrationMapper;
    private final RegistrationDateValidate registrationDateValidate;

    public void registration(UserRegistrationDto userRegistrationDto) {
        registrationDateValidate.validateRegistrationDate(userRegistrationDto);
        User user = userRegistrationMapper.toEntity(userRegistrationDto);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(userRegistrationDto.getPassword()));
        userService.save(user);
    }
}
