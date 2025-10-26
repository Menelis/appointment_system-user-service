package co.appointment.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "JWT";
    private String id;
    private String email;
    private Set<String> roles;

    public JwtResponse(final String token,
                       final String id,
                       final String email,
                       final Set<String> roles) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
