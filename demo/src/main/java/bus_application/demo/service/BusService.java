package bus_application.demo.service;

import bus_application.demo.entity.Bus;
import bus_application.demo.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;


    public Bus addBus(Bus bus) {


            bus.setAvailableSeats(bus.getTotalSeats()); // initially full
            return busRepository.save(bus);

    }

    public List<Bus> search(String source, String destination) {
        return busRepository.findBySourceAndDestination(source, destination);
    }
}