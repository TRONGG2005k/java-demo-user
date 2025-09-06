package user.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import user.demo.dto.request.OrderDetailRequest;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    UserResponse user;

    List<OrderDetailResponse> orderDetail;

    BigDecimal totalPrice;

    String address;
}
