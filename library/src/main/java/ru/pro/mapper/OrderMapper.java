package ru.pro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pro.model.dto.OrderCreateDto;
import ru.pro.model.dto.OrderDto;
import ru.pro.model.enums.OrderStatus;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, OrderStatus.class})
public interface OrderMapper {
    @Mapping(target = "id", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "status", expression = "java(OrderStatus.NEW)")
    OrderDto fromCreateDto(OrderCreateDto dto);

    default OrderDto updateStatus(OrderDto order, OrderStatus status) {
        return new OrderDto(
                order.id(),
                order.customer(),
                status
        );
    }
}
