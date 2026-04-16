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

        // 1. Fetch User & Bus
        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bus bus = busRepository.findById(booking.getBus().getId())
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        LocalDate travelDate = booking.getDate();

        // 2. Fetch Seats
        List<Long> seatIds = booking.getSeats()
                .stream()
                .map(Seat::getId)
                .toList();

        List<Seat> seats = seatRepository.findAllById(seatIds);

        if (seats.isEmpty()) {
            throw new RuntimeException("No seats selected");
        }

        // 3. Check if any seat already booked
        for (Seat seat : seats) {
            boolean isBooked = seatBookingRepository
                    .existsBySeatIdAndBusIdAndTravelDateAndStatus(
                            seat.getId(),
                            bus.getId(),
                            travelDate,
                            Status.BOOKED
                    );

            if (isBooked) {
                throw new RuntimeException("Seat already booked: " + seat.getSeatNumber());
            }
        }

        // 4. Save seat bookings
        List<SeatBooking> seatBookings = seats.stream()
                .map(seat -> SeatBooking.builder()
                        .seat(seat)
                        .bus(bus)
                        .travelDate(travelDate)
                        .status(Status.BOOKED)
                        .build())
                .toList();

        seatBookingRepository.saveAll(seatBookings);

        // 5. Prepare booking
        booking.setUser(user);
        booking.setBus(bus);
        booking.setSeats(seats);
        booking.setAmount(seats.size() * bus.getFare());
        booking.setStatus(Status.BOOKED);
        booking.setBookedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        // 6. Send email (optional — don't break booking if email fails)
        try {
            emailService.sendEmail(
                    user.getEmail(),
                    "Booking Confirmed 🎟",
                    "Hi " + user.getName() +
                            "\nYour booking is confirmed for " + travelDate
            );
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }

        return savedBooking;
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
