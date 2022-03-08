package com.example.airlinesplitter.messages;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Jacksonized
@XmlRootElement(name = "Flight")
@XmlAccessorType(XmlAccessType.FIELD)
public class Flight {
    private UUID id;
    @XmlAttribute(name = "flightNumber")
    private String flightNumber;
    @XmlAttribute(name = "flightDate")
    private String flightDate;
    @XmlElement(name = "origin")
    private String origin;
    @XmlElement(name = "destination")
    private String destination;
}
