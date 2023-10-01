package com.java.task.services;

import com.java.task.exceptions.*;
import com.java.task.models.*;
import com.java.task.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@PropertySource("classpath:application.properties")
public class UserServiceImpl implements UserService {

    @Value("${required.age}")
    private int requredAge;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional(readOnly = true)
    public List<User> showUsersList() {
        List<User> users = userRepository.findAll();
        if (users.size() == 0) {
            throw new EmptyListException("Users list is empty!");
        }
        return users;
    }

    @Transactional(readOnly = true)
    public User showUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format
                        ("There is no user in the list with id = %d", id)));
    }

    @Transactional
    public User createUser(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate userBirthDay = user.getBirthDate();

        Period period = Period.between(userBirthDay, currentDate);

        int age = period.getYears();

        if (userBirthDay.getMonthValue() > currentDate.getMonthValue() ||
                (userBirthDay.getMonthValue() == currentDate.getMonthValue() &&
                        userBirthDay.getDayOfMonth() <= currentDate.getDayOfMonth())) {
            age++;
        }

        if (age < requredAge) {
            throw new TooYoungAgeException("To check in your age should be at least 18 years old!");
        }
        return userRepository.save(user);
    }

    @Transactional
    public void updateUser(long id, User newUser) {
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format
                        ("There is no user in the list with id = %d", id)));


        if (newUser.getFirstName() != null) {
            oldUser.setFirstName(newUser.getFirstName());
        }
        if (newUser.getLastName() != null) {
            oldUser.setLastName(newUser.getLastName());
        }
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getBirthDate() != null) {
            oldUser.setBirthDate(newUser.getBirthDate());
        }
        if (newUser.getPhoneNumber() != null) {
            oldUser.setPhoneNumber(newUser.getPhoneNumber());
        }
        if (newUser.getAddress() != null) {
            Address newAddress = newUser.getAddress();

            if (newAddress.getCountry() != null) {
                oldUser.getAddress().setCountry(newAddress.getCountry());
            }
            if (newAddress.getState() != null) {
                oldUser.getAddress().setState(newAddress.getState());
            }
            if (newAddress.getCity() != null) {
                oldUser.getAddress().setCity(newAddress.getCity());
            }
            if (newAddress.getStreet() != null) {
                oldUser.getAddress().setStreet(newAddress.getStreet());
            }
            if (newAddress.getApartment() != null) {
                oldUser.getAddress().setApartment(newAddress.getApartment());
            }
        }

        userRepository.save(oldUser);
    }


    @Transactional
    public void deleteUser(long id) {
        boolean isPresent = userRepository.findById(id).isPresent();
        if (!isPresent) {
            throw new NoSuchElementException(String.format("There is no user in the list with id = %d", id));
        }

        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findUserByBirthDateRange(Map<String, String> param) {
        if (param.get("from").equals("") || param.get("to").equals("")) {
            throw new NoParamInDataRequestException("Please, set date interval parameters in your request!");
        }

        LocalDate from = LocalDate.parse(param.get("from"));
        LocalDate to = LocalDate.parse(param.get("to"));

        List<User> users = userRepository.getUsersListByBirthDateInterval(from, to);
        if (users.size() == 0) {
            throw new EmptyListException(String.format("There is no users with birth date between %s and %s!", from, to));
        }
        return users;
    }
}
