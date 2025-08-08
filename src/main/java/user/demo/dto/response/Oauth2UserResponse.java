package user.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Oauth2UserResponse {
    String id;
    String email;
    Boolean verified_email;
    String name;
    String  given_name;
    String family_name;
    String picture;
}