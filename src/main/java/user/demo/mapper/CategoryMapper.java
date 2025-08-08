package user.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import user.demo.dto.request.CategoryRequest;
import user.demo.entity.Category;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category categoryRequestToCategory(CategoryRequest request);

    void categoryRequestToCategory(@MappingTarget Category category, CategoryRequest request);
}
