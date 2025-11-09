package co.appointment.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class JwtResponse {
    @JsonProperty("access_token")
    private String accessToken;

    private String type = "JWT";
    private String id;
    private String email;
    private Set<String> roles;

    public JwtResponse(final String accessToken,
                       final String id,
                       final String email,
                       final Set<String> roles) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
