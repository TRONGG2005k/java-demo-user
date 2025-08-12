package user.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import user.demo.dto.request.ProductRequest;
import user.demo.dto.response.ProductResponse;
import user.demo.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    Product productRequestToProduct(ProductRequest request);


    ProductResponse productToProductResponse(Product product);
}
