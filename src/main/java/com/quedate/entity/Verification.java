package com.quedate.entity;

import com.quedate.entity.enums.VerificationStatus;
import com.quedate.entity.enums.VerificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VerificationType type;

    private String documentReference;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    private LocalDateTime reviewedAt;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "landlord_id", nullable = false)
    private Landlord landlord;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;
}