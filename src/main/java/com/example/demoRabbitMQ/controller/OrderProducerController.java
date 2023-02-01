package com.example.demoRabbitMQ.controller;

import com.example.demoRabbitMQ.config.MessagingConfig;
import com.example.demoRabbitMQ.model.Order;
import com.example.demoRabbitMQ.model.OrderStatus;
import com.example.demoRabbitMQ.service.EmailService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * PRODUCER
 * -----------
 * Accepts request
 * creates OrderStatus object
 * sends it to the queue which will get picked up by the consumer
 * returns 'Successfully Executed'
 * -----------
 **/
@RestController
@RequestMapping("/order")
public class OrderProducerController{

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/{restaurantName}")
    private String placeOrder(@RequestBody Order order, @PathVariable String restaurantName){
        order.setOrderId(UUID.randomUUID().toString());
//        OrderStatus orderStatus = new OrderStatus(
//                order,"ACCEPTED","order placed successfully : "+restaurantName
//        );

        OrderStatus orderStatus = new OrderStatus(
                order,"REJECTED","order cannot be placed : "+restaurantName
        );

        // When we send a message to the topic exchange, we need to pass a routing key.
        // Based on this routing key the message will be delivered to specific queues.

        // When the message's routing key matches the pattern, it will be placed in the queue.
        template.convertAndSend(MessagingConfig.EXCHANGE,MessagingConfig.ROUTING_KEY,orderStatus);
        return "Successfully executed";
    }
}


