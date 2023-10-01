package com.java.task.services;

import com.java.task.models.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> showUsersList();

    User showUser(long id);

    User createUser(User user);

    void updateUser(long id, User newUser);

    void deleteUser(long id);

    List<User> findUserByBirthDateRange(Map<String, String> param);

}
