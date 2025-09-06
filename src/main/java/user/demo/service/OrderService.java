package user.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import user.demo.dto.request.OrderRequest;
import user.demo.dto.request.OrderUpdateRequest;
import user.demo.dto.response.OrderDetailResponse;
import user.demo.dto.response.OrderResponse;
import user.demo.entity.Order;
import user.demo.entity.OrderDetail;
import user.demo.entity.User;
import user.demo.enums.OrderStatus;
import user.demo.mapper.OrderMapper;
import user.demo.mapper.ProductMapper;
import user.demo.mapper.UserMapper;
import user.demo.repository.OrderDetailRepository;
import user.demo.repository.OrderRepository;
import user.demo.repository.ProductRepository;
import user.demo.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderDetailService orderDetailService;
    private final UserService userService;
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
                .address(request.getAddress())
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
                .address(order.getAddress())
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
                .address(order.getAddress())
                .build();
    }


    public List<OrderResponse> findByUserId(){
        User user = userService.getUserFromConText();

        List<Order> orders = orderRepository.findOrdersWithDetailsByUserId(
                user.getId()
        );

        return orders.stream().map(
                order -> {
                    List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(
                                    orderDetail -> OrderDetailResponse.builder()
                                            .product(
                                                    ProductMapper.INSTANCE.productToProductResponse(
                                                            orderDetail.getProduct()
                                                    )
                                            )
                                            .quantity(orderDetail.getQuantity())
                                            .totalPrice(
                                                    orderDetail.getProduct()
                                                            .getPrice()
                                                            .multiply(BigDecimal.valueOf(
                                                                            orderDetail.getQuantity()
                                                                    )
                                                            )
                                            )
                                            .build()

                    ).toList();
                    BigDecimal totalOrderPrice = orderDetailResponses.stream()
                            .map(OrderDetailResponse::getTotalPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return OrderResponse.builder()
                            .orderDetail(orderDetailResponses)
                            .totalPrice(totalOrderPrice)
                            .user(UserMapper.INSTANCE.userToUserResponse(user))
                            .address(order.getAddress())
                            .build();
                }
        ).toList();
    }

    public OrderResponse findOrderById(Long id) {
        User user = userService.getUserFromConText();

        Order order =  orderRepository.findOrderOfUserById(user.getId(), id).orElseThrow(
                ()-> new RuntimeException("order not exist")
        );

        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(
                orderDetail -> OrderDetailResponse.builder()
                        .product(
                                ProductMapper.INSTANCE.productToProductResponse(
                                        orderDetail.getProduct()
                                )
                        )
                        .quantity(orderDetail.getQuantity())
                        .totalPrice(
                                orderDetail.getProduct()
                                        .getPrice()
                                        .multiply(BigDecimal.valueOf(
                                                        orderDetail.getQuantity()
                                                )
                                        )
                        )
                        .build()
        ).toList();

        BigDecimal totalOrderPrice = orderDetailResponses.stream()
                .map(OrderDetailResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderResponse.builder()
                .orderDetail(orderDetailResponses)
                .totalPrice(totalOrderPrice)
                .user(UserMapper.INSTANCE.userToUserResponse(user))
                .address(order.getAddress())
                .build();

    }
    public OrderResponse update(Long id, OrderRequest request){
        var orderExist = orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("order not exist")
        );
        orderExist.setAddress(request.getAddress());
        orderRepository.save(orderExist);
        return OrderMapper.INSTANCE.orderToOrderResponse(orderExist);
    }
    public  List<Order> t (Long id){
        return orderRepository.findOrdersWithDetailsByUserId(id);
    }
}
