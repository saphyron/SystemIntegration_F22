package example.org.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Jacksonized
public class DepartureGateMessage {
    private String departure;
    private String gate;
}
