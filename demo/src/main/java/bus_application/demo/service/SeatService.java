package bus_application.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import bus_application.demo.entity.Seat;
import bus_application.demo.repository.SeatRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
<<<<<<< HEAD


=======
>>>>>>> 623bcda (Updated)

    public List<Seat> getSeatsByBus(Long busId) {
        return seatRepository.findByBusId(busId);
    }
}