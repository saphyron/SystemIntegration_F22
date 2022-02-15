package com.example.airlinerouter.routes;

import com.example.airlinerouter.configuration.Constants;
import com.example.airlinerouter.messages.AirlineInformationMessage;
import com.example.airlinerouter.messages.DepartureGateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
public class AirlineMessageReceiver {

    private Logger logger = LoggerFactory.getLogger(AirlineMessageReceiver.class);

    @JmsListener(destination = Constants.FLIGHT_QUEUE_BASE+">", containerFactory = "connectionFactory")
    public void receiveFlight(AirlineInformationMessage airlineInformationMessage, Message message) {
        logger.info(" >> Original received message: " + message);
        logger.info(" >> Received airlineInformationMessage: " + airlineInformationMessage);
    }

    @JmsListener(destination = Constants.DEPARTURE_QUEUE_BASE+">", containerFactory = "connectionFactory")
    public void receiveDeparture(DepartureGateMessage departureGateMessage, Message message) {
        logger.info(" >> Original received message: " + message);
        logger.info(" >> Received departureGateMessage: " + departureGateMessage);
    }

    @JmsListener(destination = Constants.DEAD_LETTER_QUEUE_NAME, containerFactory = "connectionFactory")
    public void receiveDeadLetter(Message message) {
        logger.info(" >> DEAD Original received message: " + message);
        logger.info(" >> DEAD Received departureGateMessage: " + message);
    }
}
