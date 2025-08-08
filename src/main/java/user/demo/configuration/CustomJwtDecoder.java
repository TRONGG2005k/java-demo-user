package user.demo.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import user.demo.service.AuthService;

import javax.crypto.spec.SecretKeySpec;
// import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    protected String signerKey;

    private final AuthService authService;
    private NimbusJwtDecoder nimbusJwtDecoder;

    @PostConstruct
    public void init() {
        // Chỉ khởi tạo một lần trong phương thức @PostConstruct
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HmacSHA512");
        nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Kiểm tra xem token có hợp lệ hay không trước khi tiếp tục
            boolean isValid = authService.introspect(token);
            if (!isValid) {
                log.warn("Token is blacklisted or invalid!");
                throw new JwtException("Token không hợp lệ hoặc đã bị vô hiệu hóa!");
            }

            // Giải mã token
            return nimbusJwtDecoder.decode(token);
        } catch (Exception e) {
            log.error("Lỗi giải mã Token",e);
            throw new JwtException("Token decoding failed: " + e.getMessage());
        }
    }
}

