package com.quedate.controller.verification;

import com.quedate.dto.verification.VerificationDecisionDTO;
import com.quedate.dto.verification.VerificationRequestDTO;
import com.quedate.dto.verification.VerificationResponseDTO;
import com.quedate.entity.Landlord;
import com.quedate.exception.ForbiddenException;
import com.quedate.repository.LandlordRepository;
import com.quedate.security.UserPrincipal;
import com.quedate.service.verification.VerificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VerificationController {

    private final VerificationService verificationService;
    private final LandlordRepository landlordRepository;

    public VerificationController(
            VerificationService verificationService,
            LandlordRepository landlordRepository
    ) {
        this.verificationService = verificationService;
        this.landlordRepository = landlordRepository;
    }

    @PostMapping("/me/verification")
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<VerificationResponseDTO> requestLandlordIdentity(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody VerificationRequestDTO dto
    ) {
        Landlord landlord = landlordRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new ForbiddenException("Landlord profile not found"));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        verificationService.requestLandlordIdentity(
                                landlord,
                                dto
                        )
                );
    }

    @GetMapping("/verifications/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VerificationResponseDTO>> getPending() {
        return ResponseEntity.ok(
                verificationService.getPending()
        );
    }

    @PatchMapping("/verifications/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VerificationResponseDTO> decide(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody VerificationDecisionDTO dto
    ) {
        return ResponseEntity.ok(
                verificationService.decide(principal.getUserId(), id, dto)
        );
    }
}