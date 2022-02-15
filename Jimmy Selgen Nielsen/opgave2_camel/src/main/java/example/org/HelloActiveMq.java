package example.org;

import com.google.gson.Gson;
import example.org.messages.AirportInformationCenterMessage;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;


@SpringBootApplication
public class HelloActiveMq {

    private static final Logger logger = LoggerFactory.getLogger(HelloActiveMq.class);

    @Produce("rabbitmq:airline-information?queue=IN")
    private final ProducerTemplate producerTemplate;

    public HelloActiveMq(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public void sendMessage(String airline) {
        logger.info("Sending message at {}", LocalDateTime.now());
        Gson gson = new Gson();
        String msg = gson.toJson(new AirportInformationCenterMessage(
                airline,
                "K77",
                "Copenhagen",
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString(),
                "H11",
                "On Time"));
        producerTemplate.sendBodyAndHeader(msg, "airline", airline);
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(HelloActiveMq.class, args);
        System.out.println("Sending Airline messages.");
        HelloActiveMq helloActiveMq = context.getBean(HelloActiveMq.class);

        String[] airlines = {"SAS", "KLM", "Norwegian", "Air France", "Air Canada", "Lufthansa", "Air China", "Qantas", "Emirates", "Delta", "Southwest"};
        for (String airline : airlines) {
            logger.info("Sending message for airline {}", airline);
            helloActiveMq.sendMessage(airline);
        }

        logger.info("Waiting for confirmation ...");
        System.in.read();
        context.close();
    }
}