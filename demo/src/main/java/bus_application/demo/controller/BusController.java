package bus_application.demo.controller;



import bus_application.demo.entity.Bus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

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
