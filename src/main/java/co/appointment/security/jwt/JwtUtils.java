package co.appointment.security.jwt;

import co.appointment.config.AppConfigProperties;
import co.appointment.security.service.UserDetailsImpl;
import co.appointment.shared.constant.TokenConstants;
import co.appointment.util.ObjectUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;


@Component
@Slf4j
public class JwtUtils {
    private final AppConfigProperties.JwtSettings jwtSettings;

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
                .signWith(getSecretKey())
                .claims(claims)
                .compact();
    }
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSettings.getSecret().getBytes(StandardCharsets.UTF_8));
    }
    public Claims extractAllClaimsFromToken(final String authToken) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(authToken)
                .getPayload();
    }
    public String extractClaimByKey(final String authToken, final String claimName) {
        Claims claims = extractAllClaimsFromToken(authToken);
        Object claimValue = claims.get(claimName);
        if(claimValue == null) {
            return null;
        }
        return claimValue.toString();
    }
    public boolean validateJwtToken(final String authToken) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT token: {}", exception.getMessage());
        } catch (ExpiredJwtException exception) {
            log.error("JWT token is expired: {}", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.error("JWT token is unsupported: {}", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("JWT claims string is empty: {}", exception.getMessage());
        }
        return false;

    }
}
