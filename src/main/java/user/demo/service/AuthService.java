package user.demo.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.WebUtils;
import user.demo.client.Oauth2GoogleTokenClient;
import user.demo.dto.request.ExchangeTokenRequest;
import user.demo.dto.request.LoginRequest;
// import user.demo.dto.request.LogoutRequest;
import user.demo.dto.response.ExchangeTokenResponse;
import user.demo.dto.response.LoginResponse;
// import user.demo.dto.response.Oauth2UserResponse;
import user.demo.entity.InvalidatedToken;
import user.demo.entity.Role;
import user.demo.entity.User;
import user.demo.repository.InvalidatedTokenRepository;
import user.demo.repository.RoleRepository;
import user.demo.repository.UserRepository;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final Oauth2GoogleTokenClient googleTokenClient;
    private final RoleRepository roleRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;
    public LoginResponse login(
            LoginRequest request,
            HttpServletResponse response
    ) throws Exception {
        User user = userRepository.findByUserName(request.getUserName()).orElseThrow(
                () -> new Exception("Incorrect username or password. ")
        );
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated) {
            throw new Exception("Invalid credentials, please try again");
        }
        String refreshToken = generateToken(user, true);

        createCookie(refreshToken, response);

        return LoginResponse.builder()
                .token(generateToken(user, false))
                .build();
    }

    private String generateToken(User user, boolean isRefresh) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet.Builder claimsSet = new  JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(user.getUserName())
                .issuer("devteria.com")
                .issueTime(new Date());

        if(isRefresh) {
            claimsSet
                    .expirationTime(
                            new Date(
                                    Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                                            .toEpochMilli()
                            )
                    )
                    .claim("tokenType", "refresh");
        } else {
            claimsSet
                    .expirationTime(
                            new Date(
                                    Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS)
                                            .toEpochMilli()
                            )
                    )
                    .claim("scope", buildScope(user))
                    .claim("tokenType", "access");;
        }

        JWTClaimsSet claimsBuild = claimsSet.build();

        Payload payload = new Payload(claimsBuild.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        String id = signedJWT.getJWTClaimsSet().getJWTID();
        if(!signedJWT.verify(new MACVerifier(SIGNER_KEY.getBytes()))){
            throw new RuntimeException("invalid token");
        }

        if(expirationTime == null || expirationTime.before(new Date())){
            throw new RuntimeException("token has expired");
        }
        if(invalidatedTokenRepository.existsById(id)) {
            throw new RuntimeException("token has been disabled");
        }
        return signedJWT;
    }

    public String logout(
//            LogoutRequest logout,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ParseException, JOSEException {
//        try {
//            SignedJWT signedJWTAccess = verifyToken(logout.getToken());
//            JWTClaimsSet claimsAccessToken = signedJWTAccess.getJWTClaimsSet();
//            String idAccess = claimsAccessToken.getJWTID();
//            Date expirationTimeAccess = claimsAccessToken.getExpirationTime();
//
//            if(idAccess != null && expirationTimeAccess != null) {
//                invalidatedTokenRepository.save(InvalidatedToken.builder()
//                        .id(idAccess)
//                        .expiryTime(expirationTimeAccess)
//                        .build()
//                );
//            }
//        } catch (Exception e) {
//            log.warn("Failed to verify access token during logout: {}", e.getMessage());
//            // tiếp tục xử lý logout bình thường
//        }
        try {
            Cookie refreshTokenCookie = WebUtils.getCookie(request, "refresh_token");

            if (refreshTokenCookie != null) {
                SignedJWT signedJWTRefreshToken = verifyToken(refreshTokenCookie.getValue());
                JWTClaimsSet claimsRefreshToken = signedJWTRefreshToken.getJWTClaimsSet();

                String idRefresh = claimsRefreshToken.getJWTID();
                Date expirationRefresh = claimsRefreshToken.getExpirationTime();

                if(idRefresh != null && expirationRefresh != null){
                    invalidatedTokenRepository.save(InvalidatedToken.builder()
                            .id(idRefresh)
                            .expiryTime(expirationRefresh)
                            .build()
                    );
                }
            }
        } catch (Exception e) {
            log.warn("Failed to verify refresh token during logout: {}", e.getMessage());
        }

        clearRefreshTokenCookie(response);
        return "logout success";
    }

    public LoginResponse refreshToken(HttpServletResponse response, HttpServletRequest request) {
        String newAccessToken = null;
        String newRefreshToken;
        try {
            Cookie refreshTokenCookie = WebUtils.getCookie(request, "refresh_token");

            if (refreshTokenCookie != null && !refreshTokenCookie.getValue().isBlank()) {
                SignedJWT signedJWTRefreshToken = verifyToken(refreshTokenCookie.getValue());
                JWTClaimsSet claimsRefreshToken = signedJWTRefreshToken.getJWTClaimsSet();

                String idRefresh = claimsRefreshToken.getJWTID();
                Date expirationRefresh = claimsRefreshToken.getExpirationTime();
                String userName = claimsRefreshToken.getSubject();

                User user = userRepository.findByUserName(userName).orElseThrow(
                        () -> new RuntimeException("user not found")
                );

                if (!claimsRefreshToken.getStringClaim("tokenType").equals("refresh")) {
                    throw new JwtException("this is not a refresh token");
                }

                if (idRefresh != null && expirationRefresh != null) {
                    invalidatedTokenRepository.save(InvalidatedToken.builder()
                            .id(idRefresh)
                            .expiryTime(expirationRefresh)
                            .build()
                    );
                }

                newAccessToken = generateToken(user, false);
                newRefreshToken = generateToken(user, true);

                createCookie(newRefreshToken, response);


            }
        } catch (Exception e) {
            log.warn("Failed to verify refresh token during logout: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return LoginResponse.builder()
                .token(newAccessToken)
                .build();
    }

    public boolean introspect(String token){
        try {
            SignedJWT signedJWTRefreshToken = verifyToken(token);
            if(signedJWTRefreshToken.getJWTClaimsSet().getStringClaim("tokenType").equals("refresh")){
                throw new JwtException("Refresh Token không thể gọi API!");
            }
            return true; // Token hợp lệ
        } catch (JwtException | ParseException | JOSEException e) {
            log.error("Lỗi kiểm tra token: {}", e.getMessage());
            return false; // Token không hợp lệ
        }
    }

    public LoginResponse authenticateWithGoogle(String code, HttpServletResponse response) {
        ExchangeTokenResponse googleTokenResponse = googleTokenClient.exchangeToken(
                ExchangeTokenRequest.builder()
                        .code(code)
                        .clientId(CLIENT_ID)
                        .clientSecret(CLIENT_SECRET)
                        .grantType("authorization_code")
                        .redirectUri(REDIRECT_URI)
                        .build()
        );

        log.warn("TOKEN RESPONSE {}", googleTokenResponse);

        var userInfo = googleTokenClient.getUserInfo(
                "json", googleTokenResponse.getAccessToken()
        );

        log.warn("user {}", userInfo);

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("ROLE_USER").build()
                ));
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = userRepository.findByUserName(userInfo.getName()).orElseGet(
                () -> userRepository.save(
                        User.builder()
                                .userName(userInfo.getName())
                                .email(userInfo.getEmail())
                                .roles(roles)
                                .active(true)
                                .build()
                )
        );

        String refreshToken = generateToken(user, true);

        createCookie(refreshToken, response);

        return LoginResponse.builder()
                .token(generateToken(user, false))
                .build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", "");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        response.addCookie(refreshTokenCookie);
    }

    private void createCookie(String refreshToken, HttpServletResponse response){
        Cookie refreshTokenCookie  = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        refreshTokenCookie.setAttribute("SameSite", "Lax");
        response.addCookie(refreshTokenCookie);
    }
}