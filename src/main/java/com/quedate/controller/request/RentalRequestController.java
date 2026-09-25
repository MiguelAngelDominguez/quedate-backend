package com.quedate.controller.request;

import com.quedate.dto.request.RentalRequestCreateDTO;
import com.quedate.dto.request.RentalRequestResponseDTO;
import com.quedate.dto.request.RequestStatusUpdateDTO;
import com.quedate.entity.Landlord;
import com.quedate.entity.Student;
import com.quedate.entity.User;
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
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<RentalRequestResponseDTO> create(
            @AuthenticationPrincipal Student student,
            @PathVariable Long roomId,
            @Valid @RequestBody RentalRequestCreateDTO dto
    ) {
        RentalRequestResponseDTO response =
                rentalRequestService.create(student, roomId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/my-rental-requests")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<RentalRequestResponseDTO>> getMyRequests(
            @AuthenticationPrincipal Student student
    ) {
        return ResponseEntity.ok(
                rentalRequestService.getMyRequests(student)
        );
    }

    @GetMapping("/my-requests-landlord")
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<List<RentalRequestResponseDTO>> getLandlordRequests(
            @AuthenticationPrincipal Landlord landlord
    ) {
        return ResponseEntity.ok(
                rentalRequestService.getLandlordRequests(landlord)
        );
    }

    @PatchMapping("/rental-requests/{id}/status")
    @PreAuthorize("hasAnyRole('LANDLORD', 'ADMIN')")
    public ResponseEntity<RentalRequestResponseDTO> updateStatus(
            @AuthenticationPrincipal User actor,
            @PathVariable Long id,
            @Valid @RequestBody RequestStatusUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                rentalRequestService.updateStatus(actor, id, dto)
        );
    }
}
