package user.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import user.demo.dto.request.ProductRequest;
import user.demo.dto.request.ProductUpdateRequest;
import user.demo.dto.response.ProductResponse;
import user.demo.entity.Category;
import user.demo.entity.Product;
import user.demo.mapper.ProductMapper;
import user.demo.repository.CategoryRepository;
import user.demo.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;
    private final ImageFileService imageFileService;
    public ProductResponse createProduct(ProductRequest request){
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                ()-> new RuntimeException("category not exist")
        );

        Product product = mapper.productRequestToProduct(request);

        product.setCategory(category);
        product.setStatus("AVAILABLE");
        product.setImageUrl(imageFileService.storeFile(request.getImageUrl()));
        productRepository.save(product);

        return mapper.productToProductResponse(product);
    }

    public List<ProductResponse> findAll(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(
                mapper::productToProductResponse
        ).collect(Collectors.toList());
    }

    public ProductResponse findById(Long id){
        return mapper.productToProductResponse(
                productRepository.findById(id).orElseThrow(
                        ()-> new RuntimeException("product not found")
                )
        );
    }

    public ProductResponse update(ProductUpdateRequest request, Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("product not found")
        );

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new RuntimeException("category not found")
        );

        mapper.updateProductFromRequest(request, product);
        product.setCategory(category);
        product.setImageUrl(imageFileService.storeFile(request.getImageUrl()));

        productRepository.save(product);

        ProductResponse response = mapper.productToProductResponse(product);
        response.setStatus(request.getStatus());
        return response;
    }


    public String delete (Long id){
        productRepository.deleteById(id);
        return "deleted product with" + id;
    }
}
