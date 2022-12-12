package com.example.demoRabbitMQ.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderStatus {

    private Order order;
    private String status;
    private String message;

}
