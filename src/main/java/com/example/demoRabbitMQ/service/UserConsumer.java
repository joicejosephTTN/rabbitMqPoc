package com.example.demoRabbitMQ.service;

import com.example.demoRabbitMQ.config.MessagingConfig;
import com.example.demoRabbitMQ.exception.InvalidOrderException;
import com.example.demoRabbitMQ.model.OrderStatus;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * CONSUMER
 * ---------
 * Takes OrderStatus from Queue
 * triggers an email for each order in queue.
 * ---------
**/
@Component
public class UserConsumer {
    @Autowired
    EmailService emailService;

    // A consumer is a program that mostly waits to receive messages

    // We configure consumers using the @RabbitListener annotation.
    // The only argument passed here is the queues' name.
    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(OrderStatus orderStatus) throws InvalidOrderException {
        if(orderStatus.getStatus().equalsIgnoreCase("rejected")) {
            throw new InvalidOrderException("Order Failed: "+orderStatus.getOrder());
        } else {
            emailService.sendOrderPlacedMail(orderStatus);
            System.out.printf("Message received from queue : " + orderStatus + "\n");
        }

    }

    @RabbitListener(queues = MessagingConfig.DEAD_LETTER_QUEUE)
    public void processFailedMessages(OrderStatus orderStatus) {
        System.out.printf("Received failed message: " + orderStatus);
    }
}
