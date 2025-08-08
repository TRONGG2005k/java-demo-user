package user.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import user.demo.enums.OrderStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "total_price")
    Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus status = OrderStatus.CHO_LAY_HANG;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "order_date", updatable = false)
    LocalDateTime orderDate;

    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDateTime.now();
    }
}
