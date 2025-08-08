package user.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.demo.dto.request.IntrospectRequest;
import user.demo.dto.request.LoginRequest;
// import user.demo.dto.request.LogoutRequest;
import user.demo.dto.response.LoginResponse;
import user.demo.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/outbound/authentication")
    public LoginResponse outboundAuthenticate(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {
        return authService.authenticateWithGoogle(code, response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) throws Exception {
        return ResponseEntity.ok().body(authService.login(request, response));
    }

    @PostMapping("/introspect")
    public ResponseEntity<?> introspect(@RequestBody IntrospectRequest request) {
        return ResponseEntity.ok().body(authService.introspect(request.getToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> introspect( HttpServletResponse response,
                                         HttpServletRequest request) {
        return ResponseEntity.ok().body(authService.refreshToken(response, request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response
//            @Valid @RequestBody LogoutRequest logoutRequest
    ) throws Exception {
        return ResponseEntity.ok().body(authService.logout(request, response));
    }
}
