package com.example.airlinesplitter.messages;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@Data
@Jacksonized
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlightDetailsInfoResponse {
    @XmlElement(name = "Flight")
    private Flight flight;
    @XmlElement(name = "Passenger")
    private Passenger passenger;
    @XmlElement(name = "Luggage")
    private List<Luggage> luggageList;
}
