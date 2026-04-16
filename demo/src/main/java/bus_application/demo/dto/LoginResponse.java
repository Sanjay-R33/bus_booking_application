package bus_application.demo.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String email;

    public LoginResponse(String email){
        this.email = email;
    }
}
