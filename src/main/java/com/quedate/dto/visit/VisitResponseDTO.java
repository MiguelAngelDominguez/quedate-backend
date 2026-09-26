package com.quedate.dto.visit;

import com.quedate.entity.enums.VisitStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitResponseDTO {

    private Long id;
    private LocalDateTime scheduledAt;
    private String notes;
    private VisitStatus status;
    private Long rentalRequestId;
    private LocalDateTime createdAt;
}