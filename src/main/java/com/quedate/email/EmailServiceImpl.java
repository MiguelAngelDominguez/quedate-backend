package com.quedate.email;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            TemplateEngine templateEngine
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendHtml(
            String to,
            String subject,
            String template,
            Map<String, Object> context
    ) {
        if (!mailEnabled) {
            log.info(
                    "Email disabled. Skipping message to {} with template {}",
                    to,
                    template
            );
            return;
        }

        try {
            Context thymeleafContext = new Context();
            thymeleafContext.setVariables(context);

            String html =
                    templateEngine.process(
                            "email/" + template,
                            thymeleafContext
                    );

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            StandardCharsets.UTF_8.name()
                    );

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

            log.info("Email sent successfully to {}", to);

        } catch (Exception exception) {
            log.error(
                    "Could not send email to {}: {}",
                    to,
                    exception.getMessage()
            );
        }
    }
}