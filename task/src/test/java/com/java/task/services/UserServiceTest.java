package com.java.task.services;


import com.java.task.models.User;
import com.java.task.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    private void initUser() {
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("John")
                .email("john@mail.com")
                .birthDate(LocalDate.of(1990, 01, 01))
                .build();
    }

    @Test
    public void showUsersListTest() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.showUsersList();

        assertNotNull(foundUsers);
        assertEquals(users.size(), foundUsers.size());
    }

    @Test
    public void showUserTest() {
        long id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));

        User foundUser = userService.showUser(id);

        assertNotNull(foundUser);
        assertEquals(1, foundUser.getId());
    }

    @Test
    public void createUserTest() {
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertEquals(user, createdUser);
    }

    @Test
    public void updateUserTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User newUser = User.builder()
                .firstName("Kate")
                .lastName("Kate")
                .email("kate@mail.com")
                .birthDate(LocalDate.of(1991, 01, 01))
                .build();

        userService.updateUser(1L, newUser);

        assertEquals("Kate", user.getFirstName());

    }

    @Test
    public void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

    @Test
    public void testFindUserByBirthDateRange() {
        Map<String, String> params = new HashMap<>();
        params.put("from", "1989-01-01");
        params.put("to", "1991-01-01");

        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.getUsersListByBirthDateInterval(LocalDate.parse("1989-01-01"), LocalDate.parse("1991-01-01")))
                .thenReturn(userList);

        List<User> result = userService.findUserByBirthDateRange(params);

        assertEquals(userList, result);
    }




}
