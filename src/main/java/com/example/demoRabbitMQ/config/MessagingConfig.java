package com.example.demoRabbitMQ.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String QUEUE = "mqQueue";
    public static final String EXCHANGE="mqExchange";

    public static final String DEAD_LETTER_QUEUE ="mqDeadLetterQ";
    public static final String DEAD_LETTER_EXCHANGE ="mqDeadLetterX";
    public static final String ROUTING_KEY="mqRoutingKey";
    public static final String DEAD_LETTER_ROUTING_KEY="mqDeadLetterKey";


    // Spring AMQP handles connection-related and low-level issues out of the box,
    // for example by applying retry or requeue policies
    @Bean
    FanoutExchange deadLetterExchange() {
        return new FanoutExchange(DEAD_LETTER_EXCHANGE);
    }

    // A Dead Letter Queue (DLQ) is a queue that holds undelivered or failed messages.
    // A DLQ allows us to handle faulty or bad messages, monitor failure patterns and
    // recover from exceptions in a system.

    @Bean
    Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    @Bean
    Binding deadLetterBinding(Queue deadLetterQueue, FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
    }

    // ** amqp - not java utils

    // Giving a queue a name is important when you want to share the queue between producers and consumers.
    //  A queue is a buffer that stores messages.
    @Bean
    public Queue queue(){
        return QueueBuilder.durable(QUEUE).withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key",DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    // The producer can only send messages to an exchange. An exchange is a very simple thing.
    // On one side it receives messages from producers and on the other side it pushes them to queues.
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE);
    }

    // Now we need to tell the exchange to send messages to our queue.
    // That relationship between exchange and a queue is called a binding.
    // This can be simply read as: the queue is interested in messages from this exchange.
    // A topic exchange allows us to bind queues to it with different key patterns.
    @Bean
    public Binding binding(Queue queue,TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
