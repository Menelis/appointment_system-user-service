package co.appointment.service;

import co.appointment.shared.kafka.event.EmailEvent;
import co.appointment.config.AppConfigProperties;
import co.appointment.constant.MessageHeaderConstants;
import co.appointment.constant.RoleConstants;
import co.appointment.entity.ETokenType;
import co.appointment.entity.Role;
import co.appointment.entity.User;
import co.appointment.entity.VerificationToken;
import co.appointment.payload.request.SignInRequest;
import co.appointment.payload.request.SignUpRequest;
import co.appointment.payload.response.JwtResponse;
import co.appointment.record.ApiResponse;
import co.appointment.repository.RoleRepository;
import co.appointment.repository.UserRepository;
import co.appointment.security.jwt.JwtUtils;
import co.appointment.security.service.UserDetailsImpl;
import co.appointment.shared.service.EncryptionService;
import co.appointment.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailEventService emailEventService;
    private final AppConfigProperties appConfigProperties;
    private final VerificationTokenService verificationTokenService;
    private final EncryptionService encryptionService;

    private static final Map<String, Object> EVENT_HEADERS = Map.of(
            MessageHeaderConstants.EVENT_TYPE, MessageHeaderConstants.VERIFY_EMAIL_EVENT);

    public ApiResponse<?> signUpUser(final SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ApiResponse<>(false, String.format("Email %s is already in use", signUpRequest.getEmail()));
        }
        //TODO: Implement mapStruct
        User user = new User(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                signUpRequest.getContactNo(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        //Set default role
        Role customerRole = roleRepository.findByName(RoleConstants.CUSTOMER_ROLE).orElseThrow(() -> new RuntimeException(String.format("No role with name: %s was found in the db", RoleConstants.CUSTOMER_ROLE)));
        user.addUserRole(customerRole);
        userRepository.save(user);
        // Generate verification token
        VerificationToken verificationToken = getVerificationToken(user);
        log.info("Encrypt Key is: {}", appConfigProperties.getEncryptionKey());
        String mailBody = encryptionService.encryptText(ObjectUtils.getUserRegistrationEmailBody(ObjectUtils.getParameterizedClientUrl(appConfigProperties.getClientUrl(), user.getEmail(), verificationToken.getToken())));
        emailEventService.sendEmailEvent(
                new EmailEvent(
                        user.getEmail(),
                        "Email Verification",
                        mailBody),
                EVENT_HEADERS
        );
        return new ApiResponse<>(true, "User registered successfully.Please check your email for account verification.");
    }
    private VerificationToken getVerificationToken(final User user) {
        return verificationTokenService.createVerificationToken(
                new VerificationToken(
                        UUID.randomUUID().toString(),
                        user,
                        ObjectUtils.addMillisecondsToCurrentDate(appConfigProperties.getMail().getExpirationMs()).getTime(),
                        ETokenType.EMAIL_VERIFICATION_TOKEN.name()));
    }
    public ApiResponse<?> signInUser(final SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword())
        );
        if(!userRepository.existsByEmailAndEmailVerified(signInRequest.getEmail(), true)) {
            return new ApiResponse<>(false, "The account has not been verified. Please check your email for email registration confirmation.");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtAuthToken = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        Set<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return new ApiResponse<>(new JwtResponse(
                jwtAuthToken,
                userDetails.getId().toString(),
                userDetails.getEmail(),
                authorities));
    }
}
