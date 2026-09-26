package com.quedate.event;

import com.quedate.email.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class UserRegisteredEventListener {

    private final Optional<EmailService> emailService;

    public UserRegisteredEventListener(
            Optional<EmailService> emailService
    ) {
        this.emailService = emailService;
    }

    @EventListener
    public void handle(UserRegisteredEvent event) {
        emailService.ifPresent(service ->
                service.sendHtml(
                        event.getUser().getEmail(),
                        "Bienvenido a Quédate",
                        "welcome",
                        Map.of("user", event.getUser())
                )
        );
    }
}
