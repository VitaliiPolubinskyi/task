package com.java.task.models;



import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    @NotBlank(message = "The line should not be empty and has at least one non-space character!")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "The line should not be empty and has at least one non-space character!")
    private String lastName;

    @Column(name = "email", unique = true)
    @NotBlank(message = "Please, fill the line!")
    @Email(message = "Entered email address has an incorrect format!")
    private String email;

    @Column(name = "birth_date")
    @Past(message = "Entered date should be earlier then a current!")
    @NotNull(message = "Please, fill the line!")
    private LocalDate birthDate;

    @Embedded
    private Address address;

    @Column(name = "phone_number")
    @Pattern(regexp = "^\\d{10,12}$", message = "A phone number should consist of from 10 to 12 digits only!")
    private String phoneNumber;
}
