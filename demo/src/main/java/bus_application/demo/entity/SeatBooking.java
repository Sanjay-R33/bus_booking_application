package bus_application.demo.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"seat_id", "bus_id", "travel_date"})
        }
)
public class SeatBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Seat seat;

    @ManyToOne
    private Bus bus;

    private LocalDate travelDate;

    @Enumerated(EnumType.STRING)
    private Status status;
}
