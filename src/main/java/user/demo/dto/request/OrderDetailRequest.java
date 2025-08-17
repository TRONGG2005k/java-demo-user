package user.demo.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import user.demo.entity.Order;
import user.demo.entity.Product;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
    Long orderId;

    Long productId;

    Integer quantity;
}
