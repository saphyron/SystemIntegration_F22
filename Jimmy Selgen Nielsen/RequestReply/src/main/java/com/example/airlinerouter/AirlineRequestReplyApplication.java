package com.example.airlinerouter;

import com.example.airlinerouter.configuration.Constants;
import com.example.airlinerouter.messages.AirlineETARequest;
import com.rabbitmq.jms.admin.RMQDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import java.util.Objects;

@SpringBootApplication
public class AirlineRequestReplyApplication {
    private final Logger logger = LoggerFactory.getLogger(AirlineRequestReplyApplication.class);

    final ConnectionFactory connectionFactory;

    public AirlineRequestReplyApplication(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void sendMessage(JmsTemplate jmsTemplate) {
        jmsTemplate.setMessageIdEnabled(true);
        Destination destination = new RMQDestination(Constants.ETA_REQUEST_QUEUE_NAME, true, false);
        Destination replyToQueue = new RMQDestination(Constants.ETA_REPLY_QUEUE_NAME, true, false);

        for (String al : Constants.AIRLINES_AL) {
            jmsTemplate.send(destination, session -> {
                new AirlineETARequest();
                AirlineETARequest etaMsg = AirlineETARequest.builder()
                        .airline(al)
                        .flightNo("AT42AYH")
                        .build();

                Message msg1 = Objects.requireNonNull(jmsTemplate.getMessageConverter()).toMessage(etaMsg, session);
                msg1.setJMSReplyTo(replyToQueue);
                logger.info("Sending ETA request for airline: " + al);
                return msg1;
            });
        }
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(AirlineRequestReplyApplication.class, args);

        AirlineRequestReplyApplication app = context.getBean(AirlineRequestReplyApplication.class);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        app.sendMessage(jmsTemplate);
    }

}
