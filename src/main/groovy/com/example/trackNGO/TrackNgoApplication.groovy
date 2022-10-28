package com.example.trackNGO

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class TrackNgoApplication {

	static void main(String[] args) {
		SpringApplication.run(TrackNgoApplication, args)
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		PasswordEncoderFactories.createDelegatingPasswordEncoder()
	}

}
