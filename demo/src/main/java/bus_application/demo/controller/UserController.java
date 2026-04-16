package bus_application.demo.controller;

import bus_application.demo.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User register(@Valid @RequestBody User user) {
        return userService.register(user);
    }
}
