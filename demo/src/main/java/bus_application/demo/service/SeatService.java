package bus_application.demo.service;

import bus_application.demo.entity.Seat;
import bus_application.demo.repository.SeatRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class SeatService {

    private final SeatRepository seatRepository;



    public List<Seat> getSeatsByBus(Long busId) {
        return seatRepository.findByBusId(busId);
    }
}