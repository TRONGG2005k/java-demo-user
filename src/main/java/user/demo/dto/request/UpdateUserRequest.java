package user.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    @NotBlank(message = "Email must not be blank")
    String email;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 6, message = "User name must be more than 6 character")
    String userName;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be more than 6 character")
    String password;
}
