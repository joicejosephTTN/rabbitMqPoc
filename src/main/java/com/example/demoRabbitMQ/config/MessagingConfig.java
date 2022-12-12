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
    public static final String ROUTING_KEY="mqRoutingKey";

    // ** amqp - not java utils

    // Giving a queue a name is important when you want to share the queue between producers and consumers.
    //  A queue is a buffer that stores messages.
    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
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
