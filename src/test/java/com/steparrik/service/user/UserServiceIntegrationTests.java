package com.steparrik.service.user;

import com.steparrik.dto.user.UserRegistrationDto;
import com.steparrik.entity.User;
import com.steparrik.repository.UserRepository;
import com.steparrik.utils.exception.ApiException;
import com.steparrik.utils.mapper.user.UserRegistrationMapper;
import com.steparrik.utils.validate.RegistrationDateValidate;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Tag("integration")
public class UserServiceIntegrationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private RegistrationDateValidate registrationDateValidate;

    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        postgresContainer.start();
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("test");
        userService.save(user);
    }

    @Test
    void testFindByEmailUserExists() {
        User foundUser = userService.findByEmail("test@example.com");
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void testFindByEmailUserDoesNotExist() {
        assertThrows(ApiException.class, () -> {
            userService.findByEmail("test1@example.com");
        });
    }

    @Test
    void testRegistrationValidateDifferentPasswords() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("test@example.com");
        userRegistrationDto.setPassword("test");
        userRegistrationDto.setConfirmPassword("test1");

        ApiException exception = assertThrows(ApiException.class, () -> {
            registrationDateValidate.validateRegistrationDate(userRegistrationDto);
        });

        assertEquals("Passwords don't match", exception.getMessage());
    }

    @Test
    void testRegistrationValidateAlreadyUseEmail() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("test@example.com");
        userRegistrationDto.setPassword("test");
        userRegistrationDto.setConfirmPassword("test");

        ApiException exception = assertThrows(ApiException.class, () -> {
            registrationDateValidate.validateRegistrationDate(userRegistrationDto);
        });

        assertEquals("User with this email is already registered", exception.getMessage());
    }
}
