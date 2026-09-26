package com.quedate.controller.review;

import com.quedate.dto.rating.RatingSummaryDTO;
import com.quedate.dto.review.ReviewCreateDTO;
import com.quedate.dto.review.ReviewResponseDTO;
import com.quedate.security.UserPrincipal;
import com.quedate.service.review.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/rooms/{roomId}/reviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewResponseDTO> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long roomId,
            @Valid @RequestBody ReviewCreateDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reviewService.create(principal, roomId, dto));
    }

    @GetMapping("/rooms/{roomId}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getByRoom(
            @PathVariable Long roomId
    ) {
        return ResponseEntity.ok(
                reviewService.getByRoom(roomId)
        );
    }

    @GetMapping("/landlords/{landlordId}/rating")
    public ResponseEntity<RatingSummaryDTO> getLandlordRating(
            @PathVariable Long landlordId
    ) {
        return ResponseEntity.ok(
                reviewService.getLandlordRating(landlordId)
        );
    }
}