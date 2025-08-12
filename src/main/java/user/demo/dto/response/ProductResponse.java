package user.demo.dto.response;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import user.demo.entity.Category;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;

    String name;

    String description;

    Double price;

    Integer stockQuantity;

    String imageUrl;

    String status;

    Category category;
}
