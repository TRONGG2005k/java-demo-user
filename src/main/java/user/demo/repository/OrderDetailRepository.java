package user.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.demo.entity.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
//    List<OrderDetail> findAllByOrderId(Long id);

    @Query("""
       SELECT od FROM OrderDetail od
       JOIN FETCH od.product p
       WHERE od.order.id = :orderId
       """)
    List<OrderDetail> findAllByOrderIdWithProduct(@Param("orderId") Long orderId);

    @Query("""
    SELECT od FROM OrderDetail od
    JOIN FETCH od.order o
    JOIN FETCH o.user u
    JOIN FETCH od.product p
    WHERE u.id = :userId
    """)
    List<OrderDetail> findByUserIdWithProduct(@Param("userId") Long userId);
}
