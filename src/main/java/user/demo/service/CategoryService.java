package user.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import user.demo.dto.request.CategoryRequest;
import user.demo.entity.Category;
import user.demo.mapper.CategoryMapper;
import user.demo.repository.CategoryRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
//    private final CategoryMapper categoryMapper;

    public Category createCategory(CategoryRequest categoryRequest){
        Category category = CategoryMapper.INSTANCE.categoryRequestToCategory(categoryRequest);
        if(categoryRepository.existsByName(category.getName())){
            throw new RuntimeException("category's name existed");
        }

        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryRequest request){
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("category not found")
        );

        CategoryMapper.INSTANCE.categoryRequestToCategory(category ,request);

        return category;
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("category not found")
        );
    }

    public String delete(Long id){
        categoryRepository.deleteById(id);
        return "delete successful";
    }
}
