package com.java.task.controllers;

import com.java.task.exceptions.ValidationException;
import com.java.task.models.User;
import com.java.task.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> showUsersList() {
        List<User> users = userService.showUsersList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> showUser(@PathVariable long id) {
        User user = userService.showUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/user/new")
    public ResponseEntity<User> createUser() {
        return ResponseEntity.status(HttpStatus.OK).body(new User());
    }

    @PostMapping("/users")
    public ResponseEntity<List<User>> createUser(@Valid @RequestBody User user,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            throw new ValidationException(errors);
        }

        userService.createUser(user);
        List<User> users = userService.showUsersList();
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @GetMapping("users/{id}/edit")
    public ResponseEntity<User> editUser(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.showUser(id));
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> partialUserUpdating(@PathVariable long id,
                                                    @Valid @RequestBody User user,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            throw new ValidationException(errors);
        }

        userService.updateUser(id, user);
        User updatedUser = userService.showUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> fullUserUpdating(@PathVariable long id,
                                                 @Valid @RequestBody User user,
                                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            throw new ValidationException(errors);
        }

        userService.updateUser(id, user);
        User updatedUser = userService.showUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<List<User>> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        List<User> users = userService.showUsersList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/users_by_birthdate_range")
    public ResponseEntity<List<User>> findUserByBirthDateRange(@RequestParam("from") String from,
                                                               @RequestParam("to") String to) {
        Map<String,String> param = new HashMap<>();
        param.put("from", from);
        param.put("to", to);

        List<User> users = userService.findUserByBirthDateRange(param);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}

