package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import com.example.app.entity.Conversation;
import com.example.app.repository.ConversationRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	CommandLineRunner initAdmin(UserRepository userRepository, ConversationRepository conversationRepository) {
		return args -> {
			if (userRepository.count() == 0) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
				admin.setEmail("admin@example.com");
				admin.setRole("ADMIN");
				admin.setFullName("Administrator");
				admin.setEmployeeCode("ADMIN001");
				admin.setGender(User.Gender.Other);
				admin.setDateOfBirth(null);
				admin.setAddress("");
				admin.setMobile("");
				admin.setPhone("");
				admin.setPlaceOfBirth("");
				admin.setIdNumber("");
				admin.setIdIssuedPlace("");
				admin.setIdIssuedDate(null);
				admin.setEthnicity("");
				admin.setReligion("");
				admin.setNationality("");
				admin.setMaritalStatus("");
				admin.setEducation("");
				admin.setPermanentAddress("");
				admin.setTemporaryAddress("");
				admin.setDepartment("");
				admin.setPosition("");
				admin.setWorkStatus("");
				// Nếu User entity có trường mới, hãy bổ sung set ở đây
				userRepository.save(admin);
				System.out.println("Admin account created: admin/admin");
			}
		};
	}

	@Bean
	CommandLineRunner initSampleConversations(ConversationRepository conversationRepository, com.example.app.repository.ChatMessageRepository chatMessageRepository) {
		return args -> {
			// Không tạo cuộc hội thoại và tin nhắn mẫu nữa
		};
	}

}
