package example.org.routes;

import example.org.messages.AirportInformationCenterMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class AirlineInformationMainRoute extends RouteBuilder {

    @Override
    public void configure()  {
        errorHandler(deadLetterChannel("rabbitmq:airline-information-dead-letter?queue=dead-letter-queue")
                .maximumRedeliveries(3).redeliveryDelay(1000).retryAttemptedLogLevel(org.apache.camel.LoggingLevel.WARN));

        from("rabbitmq:airline-information?queue=IN")
                .routeId("airline-information")
                .setExchangePattern(org.apache.camel.ExchangePattern.InOnly)
                .log("Received message: ${body}")
                .to("direct:airline-information-direct")
        ;
    }
}
