package user.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import user.demo.dto.response.OrderResponse;
import user.demo.entity.Order;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "orderDetail", ignore = true)
    OrderResponse orderToOrderResponse(Order order);
}
