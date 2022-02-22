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
public class AirlineETARequest {
    private String airline;
    private String flightNo;
}
