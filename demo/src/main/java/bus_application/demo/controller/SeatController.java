package bus_application.demo.controller;

import bus_application.demo.entity.Seat;
import bus_application.demo.service.SeatService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor

public class SeatController {

    private  SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/{busId}")
    public List<Seat> getSeats(@PathVariable Long busId) {
        return seatService.getSeatsByBus(busId);
    }
}
