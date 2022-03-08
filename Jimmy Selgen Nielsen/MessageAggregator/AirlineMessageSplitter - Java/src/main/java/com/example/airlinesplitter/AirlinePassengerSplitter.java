package com.example.airlinesplitter;

import com.example.airlinesplitter.beans.MessageSplitter;
import com.example.airlinesplitter.configuration.Constants;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.xml.sax.SAXException;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@EnableJms
@SpringBootApplication
public class AirlinePassengerSplitter {

    private final Logger logger = LoggerFactory.getLogger(AirlinePassengerSplitter.class);

    final ConnectionFactory connectionFactory;

    public AirlinePassengerSplitter(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void sendMessage(JmsTemplate jmsTemplate, List<Object> objects) {
        jmsTemplate.setMessageIdEnabled(true);
        Destination replyToQueue = new ActiveMQQueue(Constants.ETA_REPLY_QUEUE_NAME) {
        };
        final String groupId = UUID.randomUUID().toString();

        for(int i = objects.size()-1; i >= 0; i--) {
            Object object = objects.get(i);
            int finalI = i;
            jmsTemplate.convertAndSend(Constants.ETA_REQUEST_QUEUE_NAME, object, message -> {
                message.setJMSReplyTo(replyToQueue);
                //message.setJMSDeliveryTime(System.currentTimeMillis() + 10000);
                message.setStringProperty("groupId", groupId);
                message.setIntProperty("partitionId", finalI);
                message.setIntProperty("totalPartitions", objects.size());
                message.setJMSDeliveryMode(1);
                return message;
            });
        };
    }

    public static void main(String[] args) throws IOException {

        ConfigurableApplicationContext context = SpringApplication.run(AirlinePassengerSplitter.class, args);
        AirlinePassengerSplitter app = context.getBean(AirlinePassengerSplitter.class);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        try {
           File file = new File("/tmp/FlightDetailsInfoResponse.xml");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String xml = "";
            while ((line = reader.readLine()) != null) {
                xml += line;
            }
            reader.close();

            MessageSplitter splitter = new MessageSplitter();

            String finalXml = xml;
            new Thread(() -> {
                try {
                    app.sendMessage(jmsTemplate, splitter.splitMessage(finalXml));
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
