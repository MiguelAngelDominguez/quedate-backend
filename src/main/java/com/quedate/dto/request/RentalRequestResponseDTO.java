package com.quedate.dto.request;

import com.quedate.entity.enums.RentalRequestStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalRequestResponseDTO {

    private Long id;
    private String message;
    private LocalDate startDate;
    private LocalDate endDate;
    private RentalRequestStatus status;
    private Long studentId;
    private Long roomId;
    private LocalDateTime createdAt;
}