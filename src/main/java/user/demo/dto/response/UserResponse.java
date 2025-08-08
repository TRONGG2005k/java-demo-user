package user.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
// import user.demo.entity.Role;

import java.util.Set;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends BaseResponse{

    Long id;

    String email;

    String userName;

    Boolean active;

    Set<RoleResponse> roles;
}
