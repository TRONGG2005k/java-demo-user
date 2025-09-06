package user.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import user.demo.entity.OrderDetail;
import user.demo.enums.OrderStatus;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
    OrderStatus status;

    String address;
}
