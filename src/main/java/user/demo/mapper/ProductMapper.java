package user.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import user.demo.dto.request.ProductRequest;
import user.demo.dto.request.ProductUpdateRequest;
import user.demo.dto.response.ProductResponse;
import user.demo.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    Product productRequestToProduct(ProductRequest request);


    ProductResponse productToProductResponse(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true) // Không đụng tới id
    @Mapping(target = "imageUrl", ignore = true)
    void updateProductFromRequest(ProductUpdateRequest request, @MappingTarget Product product);

}
