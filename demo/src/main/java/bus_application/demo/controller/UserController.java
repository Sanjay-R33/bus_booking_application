package bus_application.demo.controller;

<<<<<<< HEAD
import bus_application.demo.dto.LoginRequest;
import bus_application.demo.dto.LoginResponse;
=======
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

>>>>>>> 623bcda (Updated)
import bus_application.demo.entity.User;
import bus_application.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
<<<<<<< HEAD

=======
@RequiredArgsConstructor
>>>>>>> 623bcda (Updated)
public class UserController {

    private final UserService userService;

<<<<<<< HEAD


=======
>>>>>>> 623bcda (Updated)
    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
<<<<<<< HEAD
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
=======
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        User user = userService.authenticate(email, password);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        
        return response;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.register(user);
>>>>>>> 623bcda (Updated)
    }
}
