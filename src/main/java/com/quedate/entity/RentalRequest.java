package com.quedate.entity;

import jakarta.persistence.*;
import lombok.*;
import com.quedate.entity.enums.RentalRequestStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.AssertTrue;

@Entity
@Table(name = "rental_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private RentalRequestStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @AssertTrue(message = "End date must be greater than or equal to start date")
    public boolean isDateRangeValid() {
        return startDate != null
                && endDate != null
                && !endDate.isBefore(startDate);
    }
}