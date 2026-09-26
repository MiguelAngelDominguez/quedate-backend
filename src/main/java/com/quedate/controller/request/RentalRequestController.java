package com.quedate.controller.request;

import com.quedate.dto.request.RentalRequestCreateDTO;
import com.quedate.dto.request.RentalRequestResponseDTO;
import com.quedate.dto.request.RequestStatusUpdateDTO;
import com.quedate.security.UserPrincipal;
import com.quedate.service.request.RentalRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RentalRequestController {

    private final RentalRequestService rentalRequestService;

    public RentalRequestController(
            RentalRequestService rentalRequestService
    ) {
        this.rentalRequestService = rentalRequestService;
    }

    @PostMapping("/rooms/{roomId}/rental-requests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RentalRequestResponseDTO> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long roomId,
            @Valid @RequestBody RentalRequestCreateDTO dto
    ) {
        RentalRequestResponseDTO response =
                rentalRequestService.create(principal, roomId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/my-rental-requests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RentalRequestResponseDTO>> getMyRequests(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(
                rentalRequestService.getMyRequests(principal)
        );
    }

    @GetMapping("/my-requests-landlord")
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<List<RentalRequestResponseDTO>> getLandlordRequests(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(
                rentalRequestService.getLandlordRequests(principal)
        );
    }

    @PatchMapping("/rental-requests/{id}/status")
    @PreAuthorize("hasAnyRole('LANDLORD', 'ADMIN')")
    public ResponseEntity<RentalRequestResponseDTO> updateStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody RequestStatusUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                rentalRequestService.updateStatus(principal, id, dto)
        );
    }
}