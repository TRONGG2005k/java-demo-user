package user.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.demo.entity.Order;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long id);

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.user u " +
            "JOIN FETCH o.orderDetails od " +
            "JOIN FETCH od.product " +
            "WHERE u.id = :userId")
    List<Order> findOrdersWithDetailsByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT  o FROM Order o " +
            "JOIN FETCH o.user u " +
            "JOIN FETCH o.orderDetails od " +
            "JOIN FETCH od.product " +
            "WHERE u.id = :userId and o.id = :orderId")
    Optional<Order> findOrderOfUserById(@Param("userId") Long userId, @Param("orderId") Long orderId);
}
