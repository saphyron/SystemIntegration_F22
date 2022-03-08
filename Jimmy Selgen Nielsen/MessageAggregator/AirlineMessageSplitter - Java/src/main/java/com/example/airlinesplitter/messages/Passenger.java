package com.example.airlinesplitter.messages;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import jakarta.xml.bind.annotation.*;
import java.util.UUID;

@Data
@Jacksonized
@XmlRootElement(name = "Passenger")
@XmlAccessorType(XmlAccessType.FIELD)
public class Passenger {
    private UUID id;
    private UUID flightId;
    @XmlElement(name = "ReservationNumer")
    private String reservationNumber;
    @XmlElement(name = "FirstName")
    private String firstName;
    @XmlElement(name = "LastName")
    private String lastName;
}
