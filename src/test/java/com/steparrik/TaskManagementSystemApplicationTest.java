package com.steparrik;

import com.steparrik.entity.User;
import com.steparrik.repository.UserRepository;
import com.steparrik.service.user.UserService;
import com.steparrik.utils.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskManagementSystemApplicationTest {
    @Autowired
    private  UserService userService;
    @Autowired
    private UserRepository userRepository;

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
    }

    @Test
    void testFindByEmail_UserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("test");
        userService.save(user);

        User foundUser = userService.findByEmail("test@example.com");
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {
        String email = "test@example.com";

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.findByEmail(email);
        });

        assertEquals("User not found", exception.getMessage());
    }
}