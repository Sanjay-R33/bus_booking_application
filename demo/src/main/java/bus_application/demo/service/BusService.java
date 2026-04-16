package bus_application.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import bus_application.demo.entity.Bus;
import bus_application.demo.entity.Seat;
import bus_application.demo.entity.Status;
import bus_application.demo.repository.BusRepository;
import bus_application.demo.repository.SeatRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;
    private final SeatRepository seatRepository;


    public Bus addBus(Bus bus) {
        bus.setAvailableSeats(bus.getTotalSeats()); // initially full
        Bus savedBus = busRepository.save(bus);
        
        // Create seats for the bus
        createSeatsForBus(savedBus);
        
        return savedBus;
    }

    private void createSeatsForBus(Bus bus) {
        List<Seat> seats = new ArrayList<>();
        
        // Create seats: A1, A2, A3... B1, B2, B3... etc.
        int seatsPerRow = 5; // 5 seats per row
        int rows = (bus.getTotalSeats() + seatsPerRow - 1) / seatsPerRow;
        
        int seatCounter = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < seatsPerRow && seatCounter < bus.getTotalSeats(); col++) {
                char rowLetter = (char) ('A' + row);
                String seatNumber = rowLetter + String.valueOf(col + 1);
                
                Seat seat = new Seat();
                seat.setSeatNumber(seatNumber);
                seat.setBus(bus);
                seat.setStatus(Status.NOT_BOOKED);
                
                seats.add(seat);
                seatCounter++;
            }
        }
        
        seatRepository.saveAll(seats);
    }

    public List<Bus> search(String source, String destination) {
        return busRepository.findBySourceAndDestination(source, destination);
    }

    public List<Seat> getSeats(Long busId) {
        return seatRepository.findByBusId(busId);
    }
}