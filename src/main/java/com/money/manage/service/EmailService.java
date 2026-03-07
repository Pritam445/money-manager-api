package com.money.manage.service;

import java.io.ByteArrayInputStream;

public interface EmailService {

    public void sendEmail(String to, String subject, String body);
    public void sendEmailWithFile(String to, String subject, String body, ByteArrayInputStream attachment, String fileName);
}
