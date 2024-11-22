package com.steparrik.service.user;

import com.steparrik.dto.token.JwtDto;
import com.steparrik.dto.user.UserAuthDto;
import com.steparrik.entity.User;
import com.steparrik.entity.enums.RegisterType;
import com.steparrik.repository.UserRepository;
import com.steparrik.utils.exception.ApiException;
import com.steparrik.utils.token.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.OptionalInt;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserDetailService userDetailService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    public JwtDto createAuthToken(@RequestBody UserAuthDto authRequest) {
        Optional<User> user = userRepository.findByEmail(authRequest.getEmail());

        if(user.isPresent() && user.get().getRegisterType().equals(RegisterType.GOOGLE)){
            throw new ApiException("This email was previously registered via Google", HttpStatus.BAD_REQUEST);
        }

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ApiException("Invalid email or password", HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = userDetailService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails.getUsername());
        return new JwtDto(token);
    }
}
