package com.quedate.service.verification;

import com.quedate.dto.verification.VerificationDecisionDTO;
import com.quedate.dto.verification.VerificationRequestDTO;
import com.quedate.dto.verification.VerificationResponseDTO;
import com.quedate.entity.Landlord;
import com.quedate.entity.User;
import com.quedate.entity.Verification;
import com.quedate.entity.enums.VerificationStatus;
import com.quedate.exception.InvalidOperationException;
import com.quedate.exception.ResourceNotFoundException;
import com.quedate.repository.LandlordRepository;
import com.quedate.repository.UserRepository;
import com.quedate.repository.VerificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final LandlordRepository landlordRepository;
    private final UserRepository userRepository;

    public VerificationService(
            VerificationRepository verificationRepository,
            LandlordRepository landlordRepository,
            UserRepository userRepository
    ) {
        this.verificationRepository = verificationRepository;
        this.landlordRepository = landlordRepository;
        this.userRepository = userRepository;
    }

    public VerificationResponseDTO requestLandlordIdentity(
            Landlord landlord,
            VerificationRequestDTO dto
    ) {
        Verification verification = Verification.builder()
                .type(dto.getType())
                .documentReference(dto.getDocumentReference())
                .status(VerificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .landlord(landlord)
                .build();

        return toResponseDTO(
                verificationRepository.save(verification)
        );
    }

    public List<VerificationResponseDTO> getPending() {
        return verificationRepository
                .findByStatus(VerificationStatus.PENDING)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public VerificationResponseDTO decide(
            Long adminUserId,
            Long id,
            VerificationDecisionDTO dto
    ) {
        Verification verification = verificationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Verification not found")
                );

        VerificationStatus decision = dto.getStatus();

        if (decision != VerificationStatus.APPROVED
                && decision != VerificationStatus.REJECTED) {
            throw new InvalidOperationException(
                    "Verification decision must be APPROVED or REJECTED"
            );
        }

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        verification.setStatus(decision);
        verification.setReviewedBy(admin);
        verification.setReviewedAt(LocalDateTime.now());

        if (decision == VerificationStatus.APPROVED) {
            Landlord landlord = verification.getLandlord();
            landlord.setVerified(true);
            landlordRepository.save(landlord);
        }

        return toResponseDTO(
                verificationRepository.save(verification)
        );
    }

    private VerificationResponseDTO toResponseDTO(
            Verification verification
    ) {
        return VerificationResponseDTO.builder()
                .id(verification.getId())
                .type(verification.getType())
                .documentReference(verification.getDocumentReference())
                .status(verification.getStatus())
                .landlordId(verification.getLandlord().getId())
                .reviewedById(
                        verification.getReviewedBy() == null
                                ? null
                                : verification.getReviewedBy().getId()
                )
                .reviewedAt(verification.getReviewedAt())
                .createdAt(verification.getCreatedAt())
                .build();
    }
}