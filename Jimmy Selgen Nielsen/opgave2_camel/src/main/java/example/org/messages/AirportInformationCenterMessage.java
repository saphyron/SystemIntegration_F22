package example.org.messages;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
@ToString
public class AirportInformationCenterMessage {
    private String airline;
    private String flightNr;
    private String destination;
    private String scheduledTime;
    private String expectedTime;
    private String gate;
    private String status;
}
