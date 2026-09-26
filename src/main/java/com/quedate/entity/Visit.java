package com.quedate.entity;

import com.quedate.entity.enums.VisitStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Future
    private LocalDateTime scheduledAt;

    private String notes;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "rental_request_id", nullable = false)
    private RentalRequest rentalRequest;
}