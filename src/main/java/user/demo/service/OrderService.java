package user.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user.demo.dto.request.OrderRequest;
import user.demo.dto.response.OrderDetailResponse;
import user.demo.dto.response.OrderResponse;
import user.demo.entity.Order;
import user.demo.entity.OrderDetail;
import user.demo.entity.User;
import user.demo.enums.OrderStatus;
import user.demo.mapper.ProductMapper;
import user.demo.mapper.UserMapper;
import user.demo.repository.OrderDetailRepository;
import user.demo.repository.OrderRepository;
import user.demo.repository.ProductRepository;
import user.demo.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderDetailService orderDetailService;

    public OrderResponse createOrder(OrderRequest request){
        User userExist = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new RuntimeException("user not exist")
        );

        BigDecimal totalPrice = request.getOrderDetail().stream().map(
                d -> productRepository.findById(d.getProductId()).orElseThrow(
                        () -> new RuntimeException("product not exist")
                ).getPrice().multiply(BigDecimal.valueOf(d.getQuantity()))
        ).reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .totalPrice(totalPrice)
                .user(userExist)
                .status(OrderStatus.CHO_LAY_HANG)
                .build();

        orderRepository.save(order);
        List<OrderDetailResponse> orderDetailsResponse = request.getOrderDetail().stream().map(
                d -> {
                    d.setOrderId(order.getId()); // Gán orderId vừa tạo
                    return orderDetailService.createOrderDetail(d);
                })
                .toList();

        return OrderResponse.builder()
                .user(UserMapper.INSTANCE.userToUserResponse(userExist))
                .orderDetail(orderDetailsResponse)
                .totalPrice(totalPrice)
                .build();
    }

    public OrderResponse findById(Long id){
        Order order = orderRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("order not exist")
        );
        List<OrderDetailResponse> orderDetails = orderDetailService.findAllByOrderId(order.getId());
        return OrderResponse.builder()
                .user(UserMapper.INSTANCE.userToUserResponse(order.getUser()))
                .orderDetail(orderDetails)
                .totalPrice(order.getTotalPrice())
                .build();
    }

    public List<OrderResponse> findByUserId(Long id){
        List<OrderDetail> orderDetails = orderDetailRepository.findByUserIdWithProduct(id);

        // Group các orderDetails theo orderId
        Map<Long, List<OrderDetail>> orderMap = orderDetails.stream()
                .collect(Collectors.groupingBy(od -> od.getOrder().getId()));

        // Chuyển từng nhóm thành OrderResponse
        return orderMap.values().stream()
                .map(orderDetailList -> {
                    Order firstOrder = orderDetailList.get(0).getOrder(); // Lấy order từ phần tử đầu tiên
                    List<OrderDetailResponse> detailResponses = orderDetailList.stream()
                            .map(od -> OrderDetailResponse.builder()
                                    .quantity(od.getQuantity())
                                    .product(ProductMapper.INSTANCE.productToProductResponse(od.getProduct()))
                                    .totalPrice(
                                            od.getProduct().getPrice()
                                                    .multiply(BigDecimal.valueOf(od.getQuantity()))
                                    )
                                    .build()
                            )
                            .toList();

                    return OrderResponse.builder()
                            .user(UserMapper.INSTANCE.userToUserResponse(firstOrder.getUser()))
                            .orderDetail(detailResponses)
                            .totalPrice(firstOrder.getTotalPrice())
                            .build();
                })
                .toList();
    }

    
}
