package co.appointment.controller;

import co.appointment.record.ApiResponse;
import co.appointment.shared.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    private final EncryptionService encryptionService;

    @GetMapping("/generateSecretKey")
    public ResponseEntity<?> generateSecretKey() {
        return ResponseEntity.ok(new ApiResponse<>(encryptionService.generateEncryptionKey()));
    }
}
