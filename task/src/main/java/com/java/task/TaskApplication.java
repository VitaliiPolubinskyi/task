package com.java.task;

import com.java.task.models.Address;
import com.java.task.models.User;
import com.java.task.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}


//	@Bean
//	public CommandLineRunner run(UserRepository repository) {
//		return(args) -> {
//			addUsers(repository);
//
//			repository.findAll().forEach(System.out::println);
//		};
//	}
//
//
//	private void addUsers(UserRepository repository) {
//		User user1 = User.builder().firstName("John").lastName("John").email("john@mail.com")
//				.birthDate(LocalDate.of(1990, 12, 12)).build();
//		User user2 = User.builder().firstName("Jack").lastName("Jack").email("jack@mail.com")
//				.birthDate(LocalDate.of(1990, 12, 12)).build();
//
//		Address address3 = Address.builder().country("QQ").state("QQ")
//				.city("QQ").street("QQ").apartment("QQ").build();
//
//		User user3 = User.builder().firstName("Kate").lastName("Kate").email("kate@mail.com")
//				.birthDate(LocalDate.of(1990, 12, 12))
//				.address(address3).build();
//
//
//		repository.save(user1);
//		repository.save(user2);
//		repository.save(user3);
//	}

}
