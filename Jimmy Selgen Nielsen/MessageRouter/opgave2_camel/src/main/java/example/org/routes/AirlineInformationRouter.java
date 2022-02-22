package example.org.routes;

import example.org.configuration.Constants;
import example.org.beans.DepartureGateBean;
import example.org.messages.AirportInformationCenterMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class AirlineInformationRouter extends RouteBuilder {

    @Override
    public void configure() {
        errorHandler(deadLetterChannel("rabbitmq:airline-information-dead-letter?queue=dead-letter-queue")
                .maximumRedeliveries(3).redeliveryDelay(1000).retryAttemptedLogLevel(org.apache.camel.LoggingLevel.WARN));


        from("direct:airline-information-direct")
                .routeId("airline-information-router")
                .choice()
                    .when(header("airline").isEqualTo("SAS"))
                        .log("Routing SAS message: ${body}")
                        .to("direct:SAS")
                        .to("direct:SAS-departure")
                    .when(header("airline").isEqualTo("KLM"))
                        .log("Routing KLM message: ${body}")
                        .to("direct:KLM")
                        .to("direct:KLM-departure")
                    .when(header("airline").isEqualTo("Southwest"))
                        .log("Routing Southwest message: ${body}")
                        .to("direct:Southwest")
                        .to("direct:Southwest-departure")
                    .otherwise()
                    .log("Routing unknown message: ${body}")
                    .to("direct:${header.airline}")
                .endChoice()
        ;

        for (String airline : Constants.AIRLINES) {
            from("direct:" + airline)
                    .routeId(airline)
                    .setExchangePattern(org.apache.camel.ExchangePattern.InOnly)
                    .log(airline + " Received: ${body}")
                    .to("rabbitmq:airline-information-flight-" + airline + "?queue=flight–"+airline)
            ;
            from("direct:" + airline + "-departure")
                    .routeId(airline + "-departure")
                    .unmarshal().json(JsonLibrary.Jackson, AirportInformationCenterMessage.class)
                    .setExchangePattern(org.apache.camel.ExchangePattern.InOnly)
                    .bean(DepartureGateBean.class, "departureGateMessage(${body})")
                    .marshal().json(JsonLibrary.Jackson)
                    .log("Sending message: ${body}")
                    .to("rabbitmq:airline-information-departure-" + airline + "?queue=departure-" + airline)
            ;
        }


    }
}
