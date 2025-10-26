package co.appointment.kafka.event;

import lombok.Data;

@Data
public class EmailEvent {
    private String recipientEmail;
    private String subject;
    private String body;
}
