package com.java.task.repositories;

import com.java.task.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    @Query("from User where birthDate between :from and :to")
    List<User> getUsersListByBirthDateInterval(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
