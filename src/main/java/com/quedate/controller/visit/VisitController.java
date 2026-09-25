package com.quedate.controller.visit;

import com.quedate.dto.visit.VisitCreateDTO;
import com.quedate.dto.visit.VisitResponseDTO;
import com.quedate.dto.visit.VisitStatusUpdateDTO;
import com.quedate.entity.User;
import com.quedate.service.visit.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/rental-requests/{requestId}/visits")
    @PreAuthorize("hasAnyRole('STUDENT', 'LANDLORD')")
    public ResponseEntity<VisitResponseDTO> schedule(
            @AuthenticationPrincipal User actor,
            @PathVariable Long requestId,
            @Valid @RequestBody VisitCreateDTO dto
    ) {
        VisitResponseDTO response =
                visitService.schedule(actor, requestId, dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/visits/{visitId}/status")
    @PreAuthorize("hasAnyRole('STUDENT', 'LANDLORD')")
    public ResponseEntity<VisitResponseDTO> updateStatus(
            @AuthenticationPrincipal User actor,
            @PathVariable Long visitId,
            @Valid @RequestBody VisitStatusUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                visitService.updateStatus(actor, visitId, dto)
        );
    }

    @GetMapping("/rental-requests/{requestId}/visits")
    @PreAuthorize("hasAnyRole('STUDENT', 'LANDLORD')")
    public ResponseEntity<List<VisitResponseDTO>> getByRequest(
            @AuthenticationPrincipal User actor,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(
                visitService.getByRequest(actor, requestId)
        );
    }
}
