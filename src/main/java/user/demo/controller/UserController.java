package user.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.demo.dto.request.RegisterRequest;
import user.demo.dto.request.UpdateUserRequest;
import user.demo.dto.response.UserResponse;
// import user.demo.entity.Permission;
// import user.demo.entity.Role;
// import user.demo.entity.User;
// import user.demo.repository.RoleRepository;
// import user.demo.repository.UserRepository;
import user.demo.service.UserService;

// import java.util.HashSet;
import java.util.List;
// import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterRequest request
    ) {

        return ResponseEntity.ok().body(userService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUser() {

        return ResponseEntity.ok().body(userService.getAllUser());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) throws Exception {
        return ResponseEntity.ok().body(userService.updateUser(id, request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(
            @PathVariable("userId") Long id
    ) throws Exception {
        return ResponseEntity.ok().body(userService.delete(id));
    }

    @GetMapping("/info")
    public ResponseEntity<?> info() {
        return ResponseEntity.ok().body(userService.info());
    }
}
