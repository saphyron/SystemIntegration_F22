package example.org.routes;

import example.org.configuration.Constants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class AirlineInformationReceiver extends RouteBuilder {

    @Override
    public void configure() {
        errorHandler(deadLetterChannel("rabbitmq:airline-information-dead-letter?queue=dead-letter-queue")
                .maximumRedeliveries(3).redeliveryDelay(1000).retryAttemptedLogLevel(org.apache.camel.LoggingLevel.WARN));

        for (String airline : Constants.AIRLINES) {
            from("rabbitmq:airline-information-flight-" + airline)
                    .setExchangePattern(org.apache.camel.ExchangePattern.InOnly)
                    .routeId("airline-information-" + airline)
                    .log(airline + " Received message: ${body}")
                    //.to("file:///tmp/airline-information-flight-" + airline)
                    .to("mock:dummy");

            from("rabbitmq:airline-information-departure-" + airline)
                    .setExchangePattern(org.apache.camel.ExchangePattern.InOnly)
                    .routeId("airline-departure-" + airline)
                    .log(airline + " Received message: ${body}")
                    .to("mock:dummy");
        }
    }
}
