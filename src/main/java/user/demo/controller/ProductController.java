package user.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.demo.dto.request.ProductRequest;
import user.demo.dto.response.ProductResponse;
import user.demo.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @ModelAttribute ProductRequest request
    ){
        return ResponseEntity.ok(service.createProduct(request));
    }

//    public ResponseEntity<List<ProductResponse>> findAll(){
//        return
//    }

}
