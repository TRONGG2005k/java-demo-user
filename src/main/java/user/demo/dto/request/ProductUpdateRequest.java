package user.demo.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
import user.demo.entity.Category;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;

    String description;

    Double price;

    Integer stockQuantity;

    MultipartFile imageUrl;

    Long categoryId;
}
