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

         User savedUser=userRepository.save(user);



        return savedUser;
    }
}