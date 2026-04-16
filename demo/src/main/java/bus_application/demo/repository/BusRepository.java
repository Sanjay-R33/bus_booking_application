package bus_application.demo.repository;

import bus_application.demo.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {

    List<Bus> findBySourceAndDestination(String source, String destination);
}
