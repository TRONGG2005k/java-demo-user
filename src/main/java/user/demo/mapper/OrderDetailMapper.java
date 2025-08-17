package user.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import user.demo.dto.response.OrderDetailResponse;
import user.demo.entity.Order;
import user.demo.entity.OrderDetail;

@Mapper
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    @Mapping(target = "totalPrice", ignore = true)
    OrderDetailResponse orderDetailToOrderDetailResponse(OrderDetail orderDetail);
}
