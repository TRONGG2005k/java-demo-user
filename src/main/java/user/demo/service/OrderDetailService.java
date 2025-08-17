package user.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user.demo.dto.request.OrderDetailRequest;
import user.demo.dto.request.OrderRequest;
import user.demo.dto.response.OrderDetailResponse;
import user.demo.dto.response.OrderResponse;
import user.demo.entity.Order;
import user.demo.entity.OrderDetail;
import user.demo.entity.Product;
import user.demo.mapper.ProductMapper;
import user.demo.repository.OrderDetailRepository;
import user.demo.repository.OrderRepository;
import user.demo.repository.ProductRepository;
import user.demo.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderDetailResponse createOrderDetail(OrderDetailRequest request){
        /*
        * request có
        *
            Long productId;

            Integer quantity;
            *
            *
            * thực thể có
            *
            Long id;
           *
            Order order;

            Product product;

            Integer quantity;

            BigDecimal unitPrice;
        * */

        Product productExist = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new RuntimeException("product not exist")
        );

        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new RuntimeException("order not exist")
        );

        orderDetailRepository.save(OrderDetail.builder()
                        .order(order)
                        .product(productExist)
                        .quantity(request.getQuantity())
                        .unitPrice(productExist.getPrice())
                .build());

        return OrderDetailResponse.builder()
                .product(ProductMapper.INSTANCE.productToProductResponse(productExist))
                .quantity(request.getQuantity())
                .totalPrice(
                        productExist.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()))
                )
                .build();
    }

    public List<OrderDetailResponse> findAllByOrderId(Long id){
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderIdWithProduct(id);
        return orderDetails.stream().map(
            orderDetail -> {
                Product product = orderDetail.getProduct();
                return OrderDetailResponse.builder()
                        .product(ProductMapper.INSTANCE.
                                productToProductResponse(product)
                        )
                        .quantity(orderDetail.getQuantity())
                        .totalPrice(
                                product.getPrice()
                                        .multiply(BigDecimal.valueOf(orderDetail.getQuantity())
                                        )
                        )
                        .build();
            }
        ).toList();
    }
}
