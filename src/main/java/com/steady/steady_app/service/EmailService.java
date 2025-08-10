package com.steady.steady_app.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendOtpEmail(String toEmail, String verificationToken, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Create the Thymeleaf context
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("verificationToken", verificationToken);

        // Process the HTML template
        String emailContent = templateEngine.process("email/otp-template", context);

        // Set email properties
        helper.setTo(toEmail);
        helper.setSubject("Verify Your Email - Steady App");
        helper.setText(emailContent, true);
        helper.setFrom("${MAIL_USERNAME}");

        // Send the email
        mailSender.send(message);
    }
}