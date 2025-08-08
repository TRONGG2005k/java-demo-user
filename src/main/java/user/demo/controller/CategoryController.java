package user.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.demo.dto.request.CategoryRequest;
import user.demo.entity.Category;
import user.demo.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Valid @RequestBody CategoryRequest request
    ){
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<Category> update(
            @PathVariable("category_id") Long id,
            @Valid @RequestBody CategoryRequest request
    )
    {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(
            @PathVariable("id") Long id
    )
    {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @DeleteMapping("/{deleteId}")
    public String delete(
            @PathVariable("deleteId") Long id
    )
    {
        return categoryService.delete(id);
    }
}
