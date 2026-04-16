package bus_application.demo.controller;

import bus_application.demo.entity.Booking;
import bus_application.demo.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    @PostMapping
    public Booking book(@Valid @RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }

    @PutMapping("/{bookingId}/cancel")
    public Booking cancel(@PathVariable Long bookingId) {
        return bookingService.cancelBooking(bookingId);
    }
}
