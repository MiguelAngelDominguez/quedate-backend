package com.quedate.controller.room;

import com.quedate.entity.Publication;
import com.quedate.service.room.PublicationService;
import org.springframework.http.HttpStatus;
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
    public Publication publish(@PathVariable Long id) {
        return publicationService.publish(id);
    }

    @PostMapping("/{id}/archive")
    public Publication archive(@PathVariable Long id) {
        return publicationService.archive(id);
    }
}
