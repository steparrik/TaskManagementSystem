package com.steparrik.config.security;

import com.steparrik.dto.token.JwtDto;
import com.steparrik.entity.User;
import com.steparrik.entity.enums.RegisterType;
import com.steparrik.repository.UserRepository;
import com.steparrik.service.user.UserService;
import com.steparrik.utils.token.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    @Value("${redirect.after.check}")
    private String redirectAfterCheck;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setRegisterType(RegisterType.GOOGLE);
            userRepository.save(newUser);
        }

        return new DefaultOAuth2User(null,
                oAuth2User.getAttributes(),
                "email"
        );

    }

    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        String token = jwtTokenUtil.generateToken(email);

        response.sendRedirect("http://localhost:3000/callback?token=" + token);
    }


}
