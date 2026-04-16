package bus_application.demo.service;

import bus_application.demo.dto.LoginRequest;
import bus_application.demo.dto.LoginResponse;
import bus_application.demo.entity.User;
import bus_application.demo.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserService {

    private  UserRepository userRepository;


    public UserService(UserRepository userRepository, EmailService emailService, EmailService emailService1) {
        this.userRepository = userRepository;

    }

    public User register(User user) {
         return userRepository.save(user);
    }

    public LoginResponse login(@Valid LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return new LoginResponse(loginRequest.getEmail());

        };
    }
