package com.example.airlinerouter.routes;

import com.example.airlinerouter.configuration.Constants;
import com.example.airlinerouter.messages.AirlineETAReply;
import com.example.airlinerouter.messages.AirlineETARequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.Message;
import java.time.LocalTime;
import java.util.Objects;

@AllArgsConstructor
@Component
public class AirlineETAExchange {
    private final Logger logger = LoggerFactory.getLogger(AirlineETAExchange.class);

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = Constants.ETA_REQUEST_QUEUE_NAME, containerFactory = "connectionFactory")
    public void receiveETA(AirlineETARequest message, @Header(JmsHeaders.MESSAGE_ID) String messageId, @Header(JmsHeaders.REPLY_TO) Destination replyToQueue) {
        logger.info("Received ETA request for airline: " + message.getAirline() + ", flightNo: " + message.getFlightNo() + ", messageId: " + messageId);
        new AirlineETAReply();
        AirlineETAReply etaReply = AirlineETAReply.builder()
                .airline(message.getAirline())
                .flightNumber(message.getFlightNo())
                .expectedArrivalTime(LocalTime.now().toString())
                .build();


        jmsTemplate.send(replyToQueue, session -> {
            Message msg1 = Objects.requireNonNull(jmsTemplate.getMessageConverter()).toMessage(etaReply, session);
            msg1.setJMSCorrelationID(messageId);
            return msg1;
        });
        logger.info("Sending ETA reply for airline: " + message.getAirline() + ", flightNo: " + message.getFlightNo() + " With correlationId" + messageId);
    }


    @JmsListener(destination = Constants.ETA_REPLY_QUEUE_NAME, containerFactory = "connectionFactory")
    public void receiveETAReply(AirlineETAReply message, @Header(JmsHeaders.MESSAGE_ID) String messageId, @Header(JmsHeaders.CORRELATION_ID) String correlationId) {
        logger.info("Received ETA reply for airline: " + message.toString() + ", messageId: " + messageId + " With correlationId" + correlationId);
    }
}
