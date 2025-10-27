package co.appointment.controller;

import co.appointment.payload.request.SignInRequest;
import co.appointment.payload.request.SignUpRequest;
import co.appointment.shared.payload.response.ApiResponse;
import co.appointment.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
public class AccountControllerV1 {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid final SignUpRequest signUpRequest) {
        ApiResponse<?> apiResponse = authService.signUpUser(signUpRequest);
        if(!apiResponse.success()) {
            return ResponseEntity.badRequest().body(apiResponse);
        }
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody @Valid final SignInRequest signInRequest) {
        ApiResponse<?> apiResponse = authService.signInUser(signInRequest);
        if(!apiResponse.success()) {
            return ResponseEntity.badRequest().body(apiResponse);
        }
        return ResponseEntity.ok().body(apiResponse);
    }
}
