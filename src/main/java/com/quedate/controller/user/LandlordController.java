package com.quedate.controller.user;

import com.quedate.dto.landlord.LandlordResponseDTO;
import com.quedate.dto.landlord.LandlordUpdateDTO;
import com.quedate.security.UserPrincipal;
import com.quedate.service.user.LandlordService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/landlords")
public class LandlordController {

    private final LandlordService landlordService;

    public LandlordController(LandlordService landlordService) {
        this.landlordService = landlordService;
    }

    @PutMapping("/me/profile")
    @PreAuthorize("hasRole('LANDLORD')")
    public LandlordResponseDTO updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody LandlordUpdateDTO request
    ) {
        return landlordService.updateProfile(principal, request);
    }
}