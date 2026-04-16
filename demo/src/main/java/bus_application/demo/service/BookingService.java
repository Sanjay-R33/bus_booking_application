package bus_application.demo.service;

import bus_application.demo.entity.*;
import bus_application.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookingService {

    private  BookingRepository bookingRepository;
    private  UserRepository userRepository;
    private  BusRepository busRepository;
    private  SeatRepository seatRepository;
    private  EmailService emailService;
    private SeatBookingRepository seatBookingRepository;



    @Transactional
    public Booking createBooking(Booking booking) {

        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bus bus = busRepository.findById(booking.getBus().getId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        LocalDate travelDate = booking.getDate();

        List<Long> seatIds = booking.getSeats()
                .stream()
                .map(Seat::getId)
                .toList();

        List<Seat> seats = seatRepository.findAllById(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new RuntimeException("Some seats not found");
        }


        for (Seat seat : seats) {

            boolean alreadyBooked = seatBookingRepository
                    .existsBySeatIdAndBusIdAndTravelDateAndStatus(
                            seat.getId(),
                            bus.getId(),
                            travelDate,
                            Status.BOOKED
                    );

            if (alreadyBooked) {
                throw new IllegalStateException(
                        "Seat already booked for date: " + seat.getSeatNumber()
                );
            }
        }

        List<SeatBooking> seatBookings = seats.stream()
                .map(seat -> SeatBooking.builder()
                        .seat(seat)
                        .bus(bus)
                        .travelDate(travelDate)
                        .status(Status.BOOKED)
                        .build()
                )
                .toList();

        seatBookingRepository.saveAll(seatBookings);

        double amount = seats.size() * bus.getFare();

        booking.setUser(user);
        booking.setBus(bus);
        booking.setSeats(seats);
        booking.setAmount(amount);
        booking.setStatus(Status.BOOKED);
        booking.setBookedAt(LocalDateTime.now());

        Booking saved = bookingRepository.save(booking);

        // 📬 Email
        emailService.sendEmail(
                user.getEmail(),
                "Booking Confirmed 🎟",
                "Hi " + user.getName() + ",\n\n" +
                        "Your booking is confirmed.\n" +
                        "Date: " + travelDate + "\n" +
                        "Bus: " + bus.getSource() + " → " + bus.getDestination()
        );

        return saved;
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking not found"));

        if (booking.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("Booking already cancelled");
        }

        Bus bus = booking.getBus();
        LocalDate travelDate = booking.getDate();

        List<Long> seatIds = booking.getSeats()
                .stream()
                .map(Seat::getId)
                .toList();


        List<SeatBooking> seatBookings = seatBookingRepository
                .findByBusIdAndTravelDateAndSeatIdIn(
                        bus.getId(),
                        travelDate,
                        seatIds
                );


        for (SeatBooking sb : seatBookings) {
            sb.setStatus(Status.CANCELLED);
        }

        seatBookingRepository.saveAll(seatBookings);


        booking.setStatus(Status.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());

        Booking updated = bookingRepository.save(booking);

        // 📬 Email
        emailService.sendEmail(
                booking.getUser().getEmail(),
                "Booking Cancelled ",
                "Hi " + booking.getUser().getName() + ",\n\n" +
                        "Your booking has been cancelled.\n" +
                        "Date: " + travelDate + "\n" +
                        "Amount Refunded: ₹" + booking.getAmount()
        );

        return updated;
    }
    public List<Booking> getUserBookings(Long userId) {


        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        return bookingRepository.findByUserId(userId);
    }

    }
