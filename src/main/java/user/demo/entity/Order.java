package user.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import user.demo.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus status = OrderStatus.CHO_LAY_HANG;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "order_date", updatable = false)
    LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<OrderDetail> orderDetails;

    @Column(name = "address", nullable = false)
    String address;
    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDateTime.now();
    }
}
