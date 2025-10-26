package co.appointment.entity;

import co.appointment.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "verification_token")
@Getter
@Setter
public class VerificationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "verification_token", nullable = false, length = 100)
    private String token;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private Long expiryDate;

    @Column(name = "token_type", nullable = false, length = 100)
    private String tokenType;

    public VerificationToken() {}
    public VerificationToken(
            final String token,
            final User user,
            final Long expiryDate,
            final String tokenType) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
        this.tokenType = tokenType;
    }
}
