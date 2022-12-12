package com.example.demoRabbitMQ.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {

    private String orderId;
    private String name;
    private int qty;
    private double price;

}
