package bus_application.demo.service;

import bus_application.demo.entity.*;
import bus_application.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BusRepository busRepository;
    private final SeatRepository seatRepository;

    public Booking createBooking(Booking booking) {

        // Fetch user
        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Fetch bus
        Bus bus = busRepository.findById(booking.getBus().getId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        //Fetch seats
        List<Seat> seats = seatRepository.findAllById(
                booking.getSeats().stream().map(Seat::getId).toList()
        );

        if (seats.isEmpty()) {
            throw new RuntimeException("Seats not found");
        }

        // 💰 Calculate amount
        double amount = seats.size() * bus.getFare();

        // ✅ Set values
        booking.setUser(user);
        booking.setBus(bus);
        booking.setSeats(seats);
        booking.setAmount(amount);
        booking.setStatus(Status.BOOKED);
        booking.setBookedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == Status.CANCELLED) {
            throw new RuntimeException("Already cancelled");
        }

        booking.setStatus(Status.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());

        return bookingRepository.save(booking);

        }

    }
