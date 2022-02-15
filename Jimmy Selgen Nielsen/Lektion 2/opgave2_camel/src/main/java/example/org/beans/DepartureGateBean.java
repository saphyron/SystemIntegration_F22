package example.org.beans;

import example.org.messages.AirportInformationCenterMessage;
import example.org.messages.DepartureGateMessage;
import org.springframework.stereotype.Component;

@Component
public class DepartureGateBean {
    DepartureGateMessage departureGateMessage(AirportInformationCenterMessage airportInformationCenterMessage) {
        return new DepartureGateMessage(airportInformationCenterMessage.getFlightNr(), airportInformationCenterMessage.getGate());
    }
}
