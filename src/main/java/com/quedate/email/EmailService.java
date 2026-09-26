package com.quedate.email;

import java.util.Map;

public interface EmailService {

    void sendHtml(
            String to,
            String subject,
            String template,
            Map<String, Object> context
    );
}