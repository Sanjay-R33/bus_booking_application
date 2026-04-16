package bus_application.demo.repository;

import bus_application.demo.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByBusId(Long busId);
}
