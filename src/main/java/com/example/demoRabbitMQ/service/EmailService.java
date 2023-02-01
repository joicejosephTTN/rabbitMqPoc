package com.example.demoRabbitMQ.service;

import com.example.demoRabbitMQ.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String body, String subject) {
        logger.info("EmailService::sendMail execution started.");

        logger.debug("EmailService::sendMail configuring email details");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("joice.joseph@tothenew.com");
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        javaMailSender.send(mailMessage);

        logger.info("EmailService::sendMail execution ended.");
    }

    public void sendOrderPlacedMail(OrderStatus orderStatus){
        logger.info("EmailService::sendOrderPlacedMail execution started.");

        String subject = "Order Placed ";
        String body = "Order Placed : " + orderStatus + "\n";
        sendEmail("yorejiv920@fanneat.com", body, subject);

        logger.info("EmailService::sendOrderPlacedMail execution ended.");

    }
}
