package co.appointment.config;

import co.appointment.shared.model.CorsSettings;
import co.appointment.shared.model.JwtSettings;
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
    private String[] whiteList;

    @Data
    public static class KafkaSettings {
        private String notificationTopic;
    }
    @Data
    public static class VerificationTokenSettings {
        private Integer expirationMs;
    }
}
