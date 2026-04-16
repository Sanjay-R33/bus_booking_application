package bus_application.demo.service;

import bus_application.demo.entity.User;
import bus_application.demo.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserService {

    private  final UserRepository userRepository;
    private final EmailService emailService;



    public User register(User user) {
        // You can add password encoding later
         User savedUser=userRepository.save(user);

        emailService.sendEmail(
                savedUser.getEmail(),
                "Welcome to Bus Booking 🚍",
                "Hi " + savedUser.getName() + ",\n\nYour account has been created successfully!"
        );

        return savedUser;
    }
}