package com.example.autoparts.service;

import com.example.autoparts.dto.LoginRequest;
import com.example.autoparts.dto.RegisterRequest;
import com.example.autoparts.entity.User;
import com.example.autoparts.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        validateRegisterRequest(request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("EMAIL_ALREADY_EXISTS");
        }

        User user = new User();
        user.setEmail(request.getEmail().trim());

        // Mã hóa password trước khi lưu vào database
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setRole(User.ROLE_USER);

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        validateLoginRequest(request);

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail().trim());

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("INVALID_LOGIN");
        }

        User user = optionalUser.get();

        if (!isPasswordCorrect(request.getPassword(), user)) {
            throw new IllegalArgumentException("INVALID_LOGIN");
        }

        return user;
    }

    public void createDefaultAdminIfNotExists() {
        String adminEmail = "admin@gmail.com";

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmail(adminEmail);

            // Admin password cũng được mã hóa
            admin.setPassword(passwordEncoder.encode("admin123"));

            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setRole(User.ROLE_ADMIN);

            userRepository.save(admin);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private boolean isPasswordCorrect(String rawPassword, User user) {
        String storedPassword = user.getPassword();

        if (storedPassword == null) {
            return false;
        }

        // Nếu password trong DB đã là BCrypt
        if (storedPassword.startsWith("$2a$")
                || storedPassword.startsWith("$2b$")
                || storedPassword.startsWith("$2y$")) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        // Trường hợp cũ: password đang lưu dạng plain text
        // Nếu login đúng, tự nâng cấp password sang BCrypt
        if (storedPassword.equals(rawPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
            return true;
        }

        return false;
    }

    private void validateLoginRequest(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("EMAIL_REQUIRED");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("PASSWORD_REQUIRED");
        }
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("EMAIL_REQUIRED");
        }

        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException("INVALID_EMAIL");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("PASSWORD_TOO_SHORT");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("PASSWORD_NOT_MATCH");
        }

        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("FIRST_NAME_REQUIRED");
        }

        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("LAST_NAME_REQUIRED");
        }
    }
}