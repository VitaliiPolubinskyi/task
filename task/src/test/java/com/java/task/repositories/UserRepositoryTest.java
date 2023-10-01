package com.java.task.repositories;

import com.java.task.models.User;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource("classpath:application-test.properties")
public class UserRepositoryTest {

    private User user1;
    private User user2;

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    private void init() {
        user1 = User.builder()
                .firstName("John")
                .lastName("John")
                .email("john@mail.com")
                .birthDate(LocalDate.of(1990, 01, 01))
                .build();
       user2 = User.builder()
                .firstName("Kate")
                .lastName("Kate")
                .email("kate@mail.com")
                .birthDate(LocalDate.of(1991, 01, 01))
                .build();
    }

    @Test
    public void saveUserTest() {
        init();
        User savedUser = userRepository.save(user1);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotEqualTo(0);
    }

    @Test
    public void findUserByIdTest() {
        init();
        User savedUser = userRepository.save(user1);

        User receivedUser = userRepository.findById(savedUser.getId()).orElse(null);

        assertThat(receivedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(receivedUser.getId());

    }

    @Test
    public void findAllUsersTest() {
        init();
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void getUsersListByBirthDateIntervalTest() {
        init();
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.getUsersListByBirthDateInterval(user2.getBirthDate(), LocalDate.now());

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void deleteUserByIdTest() {
        init();
        User savedUser = userRepository.save(user1);
        userRepository.deleteById(savedUser.getId());

        User receivedUser = userRepository.findById(savedUser.getId()).orElse(null);

        assertThat(receivedUser).isNull();
    }

}

