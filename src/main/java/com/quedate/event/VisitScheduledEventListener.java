package com.quedate.event;

import com.quedate.email.EmailService;
import com.quedate.entity.Visit;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class VisitScheduledEventListener {

    private final Optional<EmailService> emailService;

    public VisitScheduledEventListener(
            Optional<EmailService> emailService
    ) {
        this.emailService = emailService;
    }

    @EventListener
    public void handle(VisitScheduledEvent event) {
        Visit visit = event.getVisit();

        String studentEmail =
                visit.getRentalRequest()
                        .getStudent()
                        .getUser()
                        .getEmail();

        String landlordEmail =
                visit.getRentalRequest()
                        .getRoom()
                        .getOwner()
                        .getUser()
                        .getEmail();

        Map<String, Object> context =
                Map.of("visit", visit);

        emailService.ifPresent(service -> {
            service.sendHtml(
                    studentEmail,
                    "Visita confirmada",
                    "visit-confirmation",
                    context
            );

            service.sendHtml(
                    landlordEmail,
                    "Visita confirmada",
                    "visit-confirmation",
                    context
            );
        });
    }
}
