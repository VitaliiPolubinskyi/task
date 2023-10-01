package com.java.task.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.task.models.Address;
import com.java.task.models.User;
import com.java.task.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

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
    public void showUsersListTest() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userService.showUsersList()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    public void showUserTest() throws Exception {
        when(userService.showUser(1L)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));
    }

    @Test
    public void createUserTest_GetRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(new User()));
    }

    @Test
    public void createUserTest_PostRequest() throws Exception {
        when(userService.createUser(user)).thenReturn(user);
        User createdUser = userService.createUser(user);
        List<User> users = Arrays.asList(createdUser);
        when(userService.showUsersList()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"));
    }

    @Test
    public void editUserTest() throws Exception {
        when(userService.showUser(1L)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));
    }

    @Test
    public void partialUserUpdatingTest() throws Exception {
        long userId = user.getId();

        User updatedUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("John")
                .email("john@mail.com")
                .birthDate(LocalDate.of(1990, 01, 01))
                .build();

        when(userService.showUser(userId)).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));;

        verify(userService, times(1)).updateUser(userId, user);
    }

    @Test
    public void fullUserUpdatingTest() throws Exception {
        long userId = user.getId();

        User updatedUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("John")
                .email("john@mail.com")
                .birthDate(LocalDate.of(1990, 01, 01))
                .phoneNumber("1234567890")
                .address(Address.builder()
                        .country("QQ")
                        .state("QQ")
                        .city("QQ")
                        .street("QQ")
                        .apartment("QQ")
                        .build())
                .build();

        when(userService.showUser(userId)).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.address.city").value("QQ"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findUserByBirthDateRangeTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("from", "1989-01-01");
        params.put("to", "1991-01-01");

        List<User> userList = Arrays.asList(user);
        when(userService.findUserByBirthDateRange(params)).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get("/users_by_birthdate_range?from=1989-01-01&to=1991-01-01"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"));

    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }


}

