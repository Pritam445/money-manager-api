package com.money.manage.service.ServiceImpl;

import com.money.manage.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Override
    public void sendEmailWithFile(
            String to,
            String subject,
            String body,
            ByteArrayInputStream attachment,
            String fileName) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true); // enable attachment


            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom(fromEmail);

            helper.addAttachment(
                    fileName,
                    new InputStreamSource() {
                        @Override
                        public java.io.InputStream getInputStream() {
                            return attachment;
                        }
                    }
            );

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void sendEmail(String to, String subject, String body) {

        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);

            javaMailSender.send(simpleMailMessage);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
