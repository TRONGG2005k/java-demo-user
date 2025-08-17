package user.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.demo.dto.request.OrderRequest;
import user.demo.dto.response.OrderResponse;
import user.demo.service.OrderService;

import java.util.List;

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

    @GetMapping("/of-user/{user_id}")
    public ResponseEntity<List<OrderResponse>> findByUserId(
            @PathVariable("user_id") Long id
    ) {
        List<OrderResponse> response = orderService.findByUserId(id);
        return ResponseEntity.ok(response);
    }
}
