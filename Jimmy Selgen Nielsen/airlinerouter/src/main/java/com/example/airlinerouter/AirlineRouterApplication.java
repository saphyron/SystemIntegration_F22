package com.example.airlinerouter;

import com.example.airlinerouter.configuration.Constants;
import com.example.airlinerouter.messages.AirlineInformationMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.time.LocalTime;

@SpringBootApplication
public class AirlineRouterApplication {

    final ConnectionFactory connectionFactory;

    public AirlineRouterApplication(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void sendMessage(JmsTemplate jmsTemplate) {
        for(String al: Constants.getAirlines()) {
            AirlineInformationMessage msg = AirlineInformationMessage.builder()
                    .airline(al)
                    .destination("CPH")
                    .expectedTime(LocalTime.now().toString())
                    .expectedTime(LocalTime.now().toString())
                    .gate("H11")
                    .flightNr("AT42AYH")
                    .status("On Time")
                    .build();
            jmsTemplate.convertAndSend(Constants.INBOUND_QUEUE_NAME, msg);
        }
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(AirlineRouterApplication.class, args);

        AirlineRouterApplication app = context.getBean(AirlineRouterApplication.class);
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        app.sendMessage(jmsTemplate);
    }

}
