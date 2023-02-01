package com.example.demoRabbitMQ.exception;

public class InvalidOrderException extends Exception{
    public InvalidOrderException(String errorMessage){
        super(errorMessage);
    }
}
