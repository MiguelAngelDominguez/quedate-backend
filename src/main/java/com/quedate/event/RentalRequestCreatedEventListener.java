package com.quedate.event;

import com.quedate.email.EmailService;
import com.quedate.entity.RentalRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class RentalRequestCreatedEventListener {

    private final Optional<EmailService> emailService;

    public RentalRequestCreatedEventListener(
            Optional<EmailService> emailService
    ) {
        this.emailService = emailService;
    }

    @EventListener
    public void handle(RentalRequestCreatedEvent event) {
        RentalRequest request = event.getRentalRequest();

        emailService.ifPresent(service ->
                service.sendHtml(
                        request.getRoom()
                                .getOwner()
                                .getUser()
                                .getEmail(),
                        "Nueva solicitud de alquiler",
                        "request-notification",
                        Map.of("rentalRequest", request)
                )
        );
    }
}
