package com.example.airlinerouter.routes;

import com.example.airlinerouter.configuration.Constants;
import com.example.airlinerouter.messages.AirlineInformationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;


@Component
public class AirlineInformationMessageRouter{
    final JmsTemplate jmsTemplate;
    private Logger logger = LoggerFactory.getLogger(AirlineInformationMessageRouter.class);

    public AirlineInformationMessageRouter(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = Constants.INBOUND_QUEUE_NAME, containerFactory = "connectionFactory")
    public void receiveAirlineInformationMessage(AirlineInformationMessage airlineMessage, Message message) throws JMSException {
        logger.info(" >> Original received message: " + message);
        logger.info(" >> Received airlineInformationMessage: " + airlineMessage);

        logger.info(" >> Sending airlineInformationMessage to DEPARTURE queue");
        jmsTemplate.convertAndSend(Constants.DEPARTURE_QUEUE_NAME, airlineMessage);

        String queue = Constants.FLIGHT_QUEUE_BASE+airlineMessage.getAirline();
        if(!Constants.getAirlines().contains(airlineMessage.getAirline())){
            queue = Constants.DEAD_LETTER_QUEUE_NAME;
        }
        logger.info(" >> Routing to "+queue);
        jmsTemplate.convertAndSend(queue, airlineMessage);
    }
}
