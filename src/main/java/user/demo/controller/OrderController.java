package user.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.demo.dto.request.OrderRequest;
import user.demo.dto.response.OrderResponse;
import user.demo.entity.Order;
import user.demo.service.OrderService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(
            @PathVariable("id") Long id
    ) {
        OrderResponse response = orderService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> findByUserId(
    ) {
        List<OrderResponse> response = orderService.findByUserId();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<OrderResponse> findMyOrderById(
            @PathVariable("id") Long id
    )
    {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    @GetMapping("/t/{id}")
    public ResponseEntity<List<Order>> t (
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(orderService.t(id));
    }
}
