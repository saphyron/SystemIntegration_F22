package com.example.airlinesplitter.beans;

import com.example.airlinesplitter.configuration.Constants;
import com.example.airlinesplitter.messages.Flight;
import com.example.airlinesplitter.messages.FlightDetailsInfoResponse;
import com.example.airlinesplitter.messages.Luggage;
import com.example.airlinesplitter.messages.Passenger;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;

@Component
public class MessageSequencer {
    private final Logger logger = LoggerFactory.getLogger(MessageSequencer.class);

    Hashtable<String, Hashtable<Integer, Object>> messages = new java.util.Hashtable<>();

    final
    JmsTemplate jmsTemplate;

    public MessageSequencer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = Constants.ETA_REQUEST_QUEUE_NAME, selector = "groupId IS NOT NULL")
    public void receiveMessages(Message msg) throws JMSException {
        String groupId = msg.getStringProperty("groupId");
        Long totalPartitions = msg.getLongProperty("totalPartitions");

        logger.info("Received message with groupId: " + groupId + ", partitionId: "+ msg.getIntProperty("partitionId")+" and totalPartitions: " + totalPartitions);
        Object message = jmsTemplate.getMessageConverter().fromMessage(msg);
        int i = msg.getIntProperty("partitionId");
        if (messages.containsKey(groupId)) {
            messages.get(groupId).put(i, message);
        } else {
            Hashtable<Integer, Object> partitionMessages = new Hashtable<>();
            partitionMessages.put(i, message);
            messages.put(groupId, partitionMessages);
        }
        if (messages.get(groupId).size() == totalPartitions) {
            logger.info("Received all messages for groupId: " + groupId);
            logger.info(messages.get(groupId).toString());
        }
    }


    private String createDocument(Deque<Object> messages) throws JAXBException {
        FlightDetailsInfoResponse response = new FlightDetailsInfoResponse();
        for (Object message : messages) {
            if (message instanceof Flight) {
                Flight flight = (Flight) message;
                response.setFlight(flight);
            }else if (message instanceof Passenger) {
                Passenger passenger = (Passenger) message;
                response.setPassenger(passenger);
            }else if (message instanceof Luggage) {
                Luggage luggage = (Luggage) message;
                if (response.getLuggageList() == null) {
                    response.setLuggageList(new ArrayList<>());
                }
                response.getLuggageList().add(luggage);
            }
        }
        try {
            StringWriter sw = new StringWriter();
            JAXBContext jaxbContext = JAXBContext.newInstance(FlightDetailsInfoResponse.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(response, sw);
            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }


}
