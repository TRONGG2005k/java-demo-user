package user.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import user.demo.dto.request.RegisterRequest;
import user.demo.dto.request.UpdateUserRequest;
import user.demo.dto.response.UserResponse;
import user.demo.entity.Role;
import user.demo.entity.User;
import user.demo.mapper.UserMapper;
import user.demo.repository.RoleRepository;
import user.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(RegisterRequest request) {
        User user = UserMapper.INSTANCE.registerRequestToUser(request);

        if (user.getActive() == null) {
            user.setActive(true);
        }

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("ROLE_USER").build()
                ));
        Set<Role> roles = new HashSet<>(); // Lấy các vai trò hiện tại của người dùng
        roles.add(role); // Thêm vai trò mới vào Set

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        UserResponse response = UserMapper.INSTANCE.userToUserResponse(user);
        response.setRoles(UserMapper.INSTANCE.rolesToRoleResponses(user.getRoles()));
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserResponse> getAllUser() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserResponse response = UserMapper.INSTANCE.userToUserResponse(user);
                    response.setRoles(UserMapper.INSTANCE.rolesToRoleResponses(user.getRoles()));
                    response.setCreatedAt(user.getCreatedAt());
                    response.setUpdatedAt(user.getUpdatedAt());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) throws Exception {
        User user = userRepository.findById(id).orElseThrow(
                () -> new Exception("user not found")
        );

        UserMapper.INSTANCE.updateUserRequestToUser(user, request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("ROLE_USER").build()
                ));
        Set<Role> roles = new HashSet<>(user.getRoles()); // Lấy các vai trò hiện tại của người dùng
        roles.add(role); // Thêm vai trò mới vào Set
        user.setRoles(roles);

        userRepository.save(user);

        UserResponse response = UserMapper.INSTANCE.userToUserResponse(user);
        response.setRoles(UserMapper.INSTANCE.rolesToRoleResponses(user.getRoles()));
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public String delete(Long id) {
        userRepository.deleteById(id);
        return "delete success";
    }

    public String disable(Long id) {
        userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("user not found")
        );
        return "disable success";
    }

    public UserResponse info() {
        var contextHolder = SecurityContextHolder.getContext();
        String name = contextHolder.getAuthentication().getName();
        User user = userRepository.findByUserName(name).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        UserResponse response = UserMapper.INSTANCE.userToUserResponse(user);
        response.setRoles(UserMapper.INSTANCE.rolesToRoleResponses(user.getRoles()));
//        response.setCreatedAt(user.getCreatedAt());
//        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public User getUserFromConText(){
        return userRepository.findByUserName(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(
                () -> new RuntimeException("user not found")
        );
    }
}
