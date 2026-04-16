package bus_application.demo.service;

import bus_application.demo.entity.*;
import bus_application.demo.repository.*;
import jakarta.transaction.Transactional;
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

    @Transactional
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

        // 🔥 CHECK each seat availability
        for (Seat seat : seats) {
            if (seat.getStatus() != Status.NOT_BOOKED) {
                throw new IllegalStateException("Seat already booked: " + seat.getSeatNumber());
            }
        }

        // 🔥 Mark seats as booked
        for (Seat seat : seats) {
            seat.setStatus(Status.BOOKED);
        }

        // 🔥 Reduce available seats
        if (bus.getAvailableSeats() < seats.size()) {
            throw new IllegalArgumentException("Not enough seats available");
        }

        bus.setAvailableSeats(bus.getAvailableSeats() - seats.size());

        double amount = seats.size() * bus.getFare();

        booking.setUser(user);
        booking.setBus(bus);
        booking.setSeats(seats);
        booking.setAmount(amount);
        booking.setStatus(Status.BOOKED);
        booking.setBookedAt(LocalDateTime.now());

        busRepository.save(bus);
        seatRepository.saveAll(seats); // 🔥 important

        Booking booked = bookingRepository.save(booking);

        // 📬 Email
        emailService.sendEmail(
                user.getEmail(),
                "Booking Confirmed 🎟",
                "Hi " + user.getName() + ",\n\n" +
                        "Your booking is confirmed.\n" +
                        "Bus: " + bus.getSource() + " → " + bus.getDestination() + "\n" +
                        "Seats: " + seats.size() + "\n" +
                        "Amount: ₹" + amount
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

        Booking cancelled= bookingRepository.save(booking);

        emailService.sendEmail(
                booking.getUser().getEmail(),
                "Booking Cancelled ❌",
                "Hi " + booking.getUser().getName() + ",\n\n" +
                        "Your booking has been cancelled.\n" +
                        "Amount Refunded: ₹" + booking.getAmount()
        );

        return cancelled;
    }
    public List<Booking> getUserBookings(Long userId) {

        // ✅ Check user exists
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        // ✅ Fetch bookings
        return bookingRepository.findByUserId(userId);
    }

    }
