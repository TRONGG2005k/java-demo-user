package user.demo.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailUpdateRequest {
    Long productId;

    Integer quantity;
}
