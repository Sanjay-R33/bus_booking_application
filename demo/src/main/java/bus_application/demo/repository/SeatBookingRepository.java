package bus_application.demo.repository;

import bus_application.demo.entity.SeatBooking;
import bus_application.demo.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {

    List<SeatBooking> findByBusIdAndTravelDate(Long busId, LocalDate travelDate);

    boolean existsBySeatIdAndBusIdAndTravelDateAndStatus(
            Long seatId, Long busId, LocalDate date, Status status
    );

    List<SeatBooking> findByBusIdAndTravelDateAndSeatIdIn(
            Long busId,
            LocalDate travelDate,
            List<Long> seatIds
    );
}
