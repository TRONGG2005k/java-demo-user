package user.demo.dto.response;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import user.demo.entity.Category;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    String name;

    String description;

    BigDecimal price;

    Integer stockQuantity;

    String imageUrl;

    String status;

    Category category;
}
