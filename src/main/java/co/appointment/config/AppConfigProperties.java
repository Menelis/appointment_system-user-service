package co.appointment.config;

import co.appointment.shared.model.CorsSettings;
import co.appointment.shared.model.OpenApiSettings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Data
public class AppConfigProperties {

    private JwtSettings jwt;
    private KafkaSettings kafka;
    private String clientUrl;
    private String encryptionKey;
    private VerificationTokenSettings verificationToken;
    private CorsSettings cors;
    private OpenApiSettings openApi;

    @Data
    public static class JwtSettings {
        private String secret;
        private int expirationMs;
    }
    @Data
    public static class KafkaSettings {
        private String notificationTopic;
    }
    @Data
    public static class VerificationTokenSettings {
        private Integer expirationMs;
    }
}
