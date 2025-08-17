package user.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.demo.dto.request.ProductRequest;
import user.demo.dto.request.ProductUpdateRequest;
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

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @Valid @ModelAttribute ProductUpdateRequest request,
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(service.update(request, id));
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ProductResponse> findById(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> delete(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(service.delete(id));
    }
}
