package co.appointment.service;

import co.appointment.entity.VerificationToken;
import co.appointment.repository.VerificationTokenRepository;
import co.appointment.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken createVerificationToken(final VerificationToken verificationToken) {
        return verificationTokenRepository.save(verificationToken);
    }
    public VerificationToken getVerificationToken(final String token) {
        return verificationTokenRepository.findByToken(token).orElse(null);
    }
    public boolean tokenExpired(final VerificationToken verificationToken) {
        LocalDateTime expiryLocalDateTime = ObjectUtils.timstampMillisToLocalDateTime(verificationToken.getExpiryDate());
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        log.debug("Current Date is:{}, Expiry Date is: {}", currentLocalDateTime, currentLocalDateTime);
        log.debug("Is Current Date after Expiry Date? {}", currentLocalDateTime.isAfter(expiryLocalDateTime));
        return currentLocalDateTime.isAfter(expiryLocalDateTime);
    }
}
