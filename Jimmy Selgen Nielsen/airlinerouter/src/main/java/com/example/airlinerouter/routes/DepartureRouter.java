package com.example.airlinerouter.routes;

import com.example.airlinerouter.configuration.Constants;
import com.example.airlinerouter.messages.AirlineInformationMessage;
import com.example.airlinerouter.messages.DepartureGateMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
public class DepartureRouter {

    final JmsTemplate jmsTemplate;

    public DepartureRouter(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = Constants.DEPARTURE_QUEUE_NAME, containerFactory = "connectionFactory")
    public void receiveAirlineNotification(AirlineInformationMessage airlineInformationMessage, Message message){
        String queue = Constants.DEPARTURE_QUEUE_BASE+airlineInformationMessage.getAirline();
        DepartureGateMessage msg = DepartureGateMessage.builder().departure(airlineInformationMessage.getFlightNr()).gate(airlineInformationMessage.getGate()).build();
        jmsTemplate.convertAndSend(queue, msg);
    }
}
