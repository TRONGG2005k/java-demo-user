package user.demo.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import user.demo.dto.request.ExchangeTokenRequest;
import user.demo.dto.response.ExchangeTokenResponse;
import user.demo.dto.response.Oauth2UserResponse;

@Service
@RequiredArgsConstructor
public class Oauth2GoogleTokenClient {
    private final RestTemplate restTemplate;

    public ExchangeTokenResponse exchangeToken(ExchangeTokenRequest request) {
        String url = "https://oauth2.googleapis.com/token";

        // Chuyển ExchangeTokenRequest thành MultiValueMap
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", request.getCode());
        map.add("client_id", request.getClientId());
        map.add("client_secret", request.getClientSecret());
        map.add("redirect_uri", request.getRedirectUri());
        map.add("grant_type", request.getGrantType());

        // Cấu hình header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Tạo HttpEntity với map dữ liệu và header
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        // Gửi request và nhận response đồng bộ
        return restTemplate.exchange(
                url, HttpMethod.POST, entity, ExchangeTokenResponse.class
        ).getBody(); // Trả về body của response
    }

    public Oauth2UserResponse getUserInfo(String alt, String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Oauth2UserResponse.class
        ).getBody();
    }
}
