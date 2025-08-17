package user.demo.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String name;

    String description;

    BigDecimal price;

    Integer stockQuantity;

    MultipartFile imageUrl;

    Long categoryId;

    String status;
}
