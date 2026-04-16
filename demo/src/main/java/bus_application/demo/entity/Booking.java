
package bus_application.demo.entity;

import bus_application.demo.entity.User;
import bus_application.demo.entity.Bus;
import bus_application.demo.entity.Seat;
import bus_application.demo.entity.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many bookings → One user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many bookings → One bus
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    // Many bookings ↔ Many seats
    @ManyToMany
    @JoinTable(
            name = "booking_seats",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    private List<Seat> seats;

    @NotNull
    private LocalDate date;

    @Positive
    private double amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime bookedAt;

    private LocalDateTime cancelledAt;
}