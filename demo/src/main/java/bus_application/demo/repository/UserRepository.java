package bus_application.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import java.util.Optional;
=======
import bus_application.demo.entity.User;
>>>>>>> 623bcda (Updated)

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
