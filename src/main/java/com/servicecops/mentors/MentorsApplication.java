package com.servicecops.mentors;

import com.servicecops.mentors.models.jpahelpers.repository.JetRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(repositoryBaseClass = JetRepositoryImpl.class)
@SpringBootApplication
public class MentorsApplication {
	public static void main(String[] args) {
		SpringApplication.run(MentorsApplication.class, args);
	}
}
