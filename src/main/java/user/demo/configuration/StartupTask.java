package user.demo.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import user.demo.entity.Permission;
import user.demo.entity.Role;
import user.demo.entity.User;
import user.demo.repository.RoleRepository;
import user.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartupTask {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    
    @PostConstruct
    public void init(){
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseGet(
                ()->roleRepository.save(
                        Role.builder()
                                .name("ROLE_ADMIN")
                                .build()
                )
        );

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        userRepository.findByUserName("admin").orElseGet(
                () -> userRepository.save(
                        User.builder()
                                .userName("admin")
                                .password(passwordEncoder.encode("admin"))
                                .email("admin@example.com")
                                .roles(roles)
                                .build()
                )
        );
    }
}
