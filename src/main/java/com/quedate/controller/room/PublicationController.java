package com.quedate.controller.room;

import com.quedate.entity.Publication;
import com.quedate.security.UserPrincipal;
import com.quedate.service.room.PublicationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/my-rooms")
public class PublicationController {

    private final PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @PostMapping("/{id}/publish")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('LANDLORD', 'ADMIN')")
    public Publication publish(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return publicationService.publish(id, principal);
    }

    @PostMapping("/{id}/archive")
    @PreAuthorize("hasAnyRole('LANDLORD', 'ADMIN')")
    public Publication archive(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return publicationService.archive(id, principal);
    }
}