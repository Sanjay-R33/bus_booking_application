package bus_application.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus_application.demo.entity.Seat;
import bus_application.demo.service.SeatService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;
<<<<<<< HEAD


=======
>>>>>>> 623bcda (Updated)

    @GetMapping("/{busId}")
    public List<Seat> getSeats(@PathVariable Long busId) {
        return seatService.getSeatsByBus(busId);
    }
}
