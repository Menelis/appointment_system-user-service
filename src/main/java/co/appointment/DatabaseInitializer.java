package co.appointment;

import co.appointment.shared.constant.RoleConstants;
import co.appointment.entity.Role;
import co.appointment.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        seedRoles();
    }
    private void seedRoles() {
        Set<String> roles = Set.of(
                RoleConstants.ADMIN_ROLE,
                RoleConstants.USER_ROLE,
                RoleConstants.CUSTOMER_ROLE);
        roles.forEach(role -> {
            Optional<Role> dbRole = roleRepository.findByName(role);
            if(dbRole.isEmpty()) {
                roleRepository.save(new Role(role));
            }
        });
    }
}
