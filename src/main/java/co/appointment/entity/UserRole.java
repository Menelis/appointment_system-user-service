package co.appointment.entity;

import co.appointment.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
public class UserRole extends BaseEntity {

    @EmbeddedId
    private UserRoleKey id; //Composite id

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    private Role role;

    public UserRole() {}

    public UserRole(final User user, final Role role, final UserRoleKey id) {
        this.user = user;
        this.role = role;
        this.id = id;
    }
}
