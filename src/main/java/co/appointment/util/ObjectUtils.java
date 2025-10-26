package co.appointment.util;

import co.appointment.kafka.event.EmailEvent;

import java.util.Date;

public class ObjectUtils {
    public static Date addMillisecondsToCurrentDate(final int expiryTimeInMills) {
        Date now = new Date();
        return new Date((now).getTime() + expiryTimeInMills);
    }

    public static EmailEvent createEmailEvent(final String recipientEmail, final String subject, final String body) {
        EmailEvent event = new EmailEvent();
        event.setSubject(subject);
        event.setBody(body);
        event.setRecipientEmail(recipientEmail);
        return event;
    }
    public static String getUserRegistrationEmailBody(final String clientUrl) {
        return "Hi,<br/><br/>" +
                String.format("Welcome to Appointment System.To complete registration process click <a href='%s'>here</a> to verify your account.<br/></br/><br/><br/>", clientUrl) +
                "Kind Regards,<br/>" +
                "Appointment System Team.";
    }
}
