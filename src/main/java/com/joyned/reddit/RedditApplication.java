package com.joyned.reddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing
@SpringBootApplication
public class RedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditApplication.class, args);
	}

}
