package com.example.airlinerouter.messages;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class AirlineETAReply {
    private String airline;
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private String departureTime;
    private String expectedArrivalTime;
}
