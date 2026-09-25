package com.quedate.dto.verification;

import com.quedate.entity.enums.VerificationStatus;
import com.quedate.entity.enums.VerificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResponseDTO {

    private Long id;
    private VerificationType type;
    private String documentReference;
    private VerificationStatus status;
    private Long landlordId;
    private Long reviewedById;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
}