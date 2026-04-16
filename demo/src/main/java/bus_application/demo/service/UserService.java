package bus_application.demo.service;

<<<<<<< HEAD
import bus_application.demo.dto.LoginRequest;
import bus_application.demo.dto.LoginResponse;
import bus_application.demo.entity.User;
import bus_application.demo.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
=======
import org.springframework.stereotype.Service;

import bus_application.demo.entity.User;
import bus_application.demo.repository.UserRepository;
>>>>>>> 623bcda (Updated)
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

<<<<<<< HEAD
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
=======
    private final UserRepository userRepository;
    private final EmailService emailService;

    public User register(User user) {
        // You can add password encoding later
        User savedUser = userRepository.save(user);

        emailService.sendEmail(
                savedUser.getEmail(),
                "Welcome to Bus Booking 🚍",
                "Hi " + savedUser.getName() + ",\n\nYour account has been created successfully!"
        );

        return savedUser;
    }

    public User authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }
}
>>>>>>> 623bcda (Updated)
