package bus_application.demo.controller;



import bus_application.demo.entity.Bus;
import bus_application.demo.service.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buses")
@RequiredArgsConstructor
public class BusController {

    private BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @PostMapping
    public Bus addBus(@Valid @RequestBody Bus bus) {
        return busService.addBus(bus);
    }

    @GetMapping("/search")
    public List<Bus> search(
            @RequestParam String source,
            @RequestParam String destination) {

        return busService.search(source, destination);
    }
}
