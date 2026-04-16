package bus_application.demo.controller;



import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bus_application.demo.entity.Bus;
import bus_application.demo.entity.Seat;
import bus_application.demo.service.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;
<<<<<<< HEAD


=======
>>>>>>> 623bcda (Updated)

    @PostMapping
    public Bus addBus(@Valid @RequestBody Bus bus) {
        return busService.addBus(bus);
    }

    @GetMapping("/search")
    public List<Bus> search(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(required = false) String travelDate) {

        return busService.search(source, destination);
    }

    @GetMapping("/{busId}/seats")
    public List<Seat> getSeats(@PathVariable Long busId) {
        return busService.getSeats(busId);
    }
}
