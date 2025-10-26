package co.appointment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Data
public class AppConfigProperties {

    private JwtSettings jwt;
    private KafkaSettings kafka;
    private String clientUrl;
    @Data
    public static class JwtSettings {
        private String secret;
        private int expirationMs;
    }
    @Data
    public static class KafkaSettings {
        private String notificationTopic;
    }
}
