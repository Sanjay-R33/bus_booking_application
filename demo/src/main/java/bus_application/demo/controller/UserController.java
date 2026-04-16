package bus_application.demo.controller;

import bus_application.demo.entity.User;
import bus_application.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")

public class UserController {

    private final UserService userService;



    @PostMapping
    public User register(@Valid @RequestBody User user) {
        return userService.register(user);
    }
}
