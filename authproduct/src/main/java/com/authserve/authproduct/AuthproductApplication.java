package com.authserve.authproduct;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.authserve.authproduct.models.RoleEntity;
import com.authserve.authproduct.models.RoleEnum;
import com.authserve.authproduct.models.UserEntity;
import com.authserve.authproduct.repository.UserRepository;

@SpringBootApplication
public class AuthproductApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthproductApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			RoleEntity roleUser = RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.build();
			RoleEntity roleInvited = RoleEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.build();
			UserEntity userLinda = UserEntity.builder().username("linda")
					.password("$2a$10$8ZYpRN.oJz6xbn5PgcIJ8.wOrtcZMV0BlFlYfChA91bm91NiTO4LS")
					.email("linda@example.com")
					.roles(Set.of(roleUser))
					.build();
			UserEntity userLuis = UserEntity.builder()
					.username("luis")
					.password("$2a$10$8ZYpRN.oJz6xbn5PgcIJ8.wOrtcZMV0BlFlYfChA91bm91NiTO4LS")
					.email("luis@example.com")
					.roles(Set.of(roleInvited))
					.build();
			userRepository.saveAll(List.of(userLinda, userLuis));

		};
	}

}
