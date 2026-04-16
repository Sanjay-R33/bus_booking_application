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

    private  BookingRepository bookingRepository;
    private  UserRepository userRepository;
    private  BusRepository busRepository;
    private  SeatRepository seatRepository;
    private  EmailService emailService;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, BusRepository busRepository, SeatRepository seatRepository, EmailService emailService){
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.busRepository = busRepository;
        this.seatRepository = seatRepository;
        this.emailService = emailService;
    }

    public Booking createBooking(Booking booking) {

        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bus bus = busRepository.findById(booking.getBus().getId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        List<Long> seatIds = booking.getSeats()
                .stream()
                .map(Seat::getId)
                .toList();

        List<Seat> seats = seatRepository.findAllById(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new RuntimeException("Some seats not found");
        }

        // ❗ CHECK availability
        if (bus.getAvailableSeats() < seats.size()) {
            throw new RuntimeException("Not enough seats available");
        }

        // 💺 Reduce seats
        bus.setAvailableSeats(bus.getAvailableSeats() - seats.size());

        double amount = seats.size() * bus.getFare();

        booking.setUser(user);
        booking.setBus(bus);
        booking.setSeats(seats);
        booking.setAmount(amount);
        booking.setStatus(Status.BOOKED);
        booking.setBookedAt(LocalDateTime.now());

        // save both
        busRepository.save(bus);

        Booking booked= bookingRepository.save(booking);

        emailService.sendEmail(
                booking.getUser().getEmail(),
                "Booking Confirmed 🎟",
                "Hi " + booking.getUser().getName() + ",\n\n" +
                        "Your booking is confirmed.\n" +
                        "Bus: " + booking.getBus().getSource() + " → " + booking.getBus().getDestination() + "\n" +
                        "Seats: " + booking.getSeats().size() + "\n" +
                        "Amount: ₹" + booking.getAmount()
        );

        return booked;
    }

    public Booking cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == Status.CANCELLED) {
            throw new RuntimeException("Already cancelled");
        }

        Bus bus = booking.getBus();

        // Increase seats back
        int seatsToReturn = booking.getSeats().size();
        bus.setAvailableSeats(bus.getAvailableSeats() + seatsToReturn);

        booking.setStatus(Status.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());

        busRepository.save(bus);

        return bookingRepository.save(booking);
    }

    }
