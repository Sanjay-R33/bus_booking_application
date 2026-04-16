package bus_application.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus_application.demo.entity.Booking;
import bus_application.demo.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;



    @PostMapping
    public Booking book(@Valid @RequestBody Booking booking,
                       @RequestHeader(value = "X-User-Id", required = false) String userId) {
        // userId is passed in the header but the booking object should contain user info
        return bookingService.createBooking(booking);
    }

    @GetMapping("/me")
    public List<Booking> getMyBookings(@RequestHeader("X-User-Id") Long userId) {
        return bookingService.getUserBookings(userId);
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
