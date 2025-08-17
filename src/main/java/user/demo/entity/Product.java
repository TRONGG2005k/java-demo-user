package user.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "product")
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    Integer stockQuantity;

    @Column(name = "image_url", nullable = false)
    String imageUrl;

    String status;

    @ManyToOne
    @JoinColumn(name = "category_id",  nullable = false)
    Category category;
}
