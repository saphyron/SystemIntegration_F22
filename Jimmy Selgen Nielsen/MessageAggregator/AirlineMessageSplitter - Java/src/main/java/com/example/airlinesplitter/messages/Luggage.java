package com.example.airlinesplitter.messages;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Jacksonized
@XmlRootElement(name = "Luggage")
@XmlAccessorType(XmlAccessType.FIELD)
public class Luggage {
    @XmlElement(name = "id")
    private String id;
    @XmlElement(name = "identification")
    private String identification;
    @XmlElement(name = "category")
    private String category;
    @XmlElement(name = "weight")
    private Double weight;
    private UUID passengerId;
    private UUID flightId;
}
