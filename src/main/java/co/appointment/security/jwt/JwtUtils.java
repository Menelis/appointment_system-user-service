package co.appointment.security.jwt;

import co.appointment.config.AppConfigProperties;
import co.appointment.shared.security.UserDetailsImpl;
import co.appointment.shared.constant.TokenConstants;
import co.appointment.shared.model.JwtSettings;
import co.appointment.shared.util.SharedJwtUtils;
import co.appointment.util.ObjectUtils;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;



@Component
@Slf4j
public class JwtUtils {
    private final JwtSettings jwtSettings;

    public JwtUtils(final AppConfigProperties appConfigProperties) {
        this.jwtSettings = appConfigProperties.getJwt();
    }

    public String generateJwtToken(final Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Map<String, Object> claims = Map.of(
                TokenConstants.FULL_NAME, String.format("%s %s", userPrincipal.getFirstName(), userPrincipal.getLastName()),
                TokenConstants.ROLES, userPrincipal.getAuthorities(),
                TokenConstants.EMAIL, userPrincipal.getEmail()
        );
        Date now = new Date();
        return Jwts.builder()
                .subject(userPrincipal.getId().toString())
                .issuedAt(now)
                .expiration(ObjectUtils.addMillisecondsToCurrentDate(jwtSettings.getExpirationMs()))
                .signWith(SharedJwtUtils.generateSecretKey(jwtSettings.getSecret()))
                .claims(claims)
                .compact();
    }
    public String extractClaimByKey(final String authToken, final String claimName) {
        return SharedJwtUtils.extractClaimByKey(authToken, claimName, jwtSettings.getSecret());
    }
    public boolean validateJwtToken(final String authToken) {
      return SharedJwtUtils.validateToken(authToken, jwtSettings.getSecret());
    }
}
