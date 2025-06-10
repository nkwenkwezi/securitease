package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "orderIds", expression = "java(mapOrderIds(product.getOrders()))")
    ProductDTO toDto(Product product);

    List<ProductDTO> toDtoList(List<Product> products);

    default Set<Long> mapOrderIds(Set<Order> orders) {
        if (orders == null) return Set.of();
        return orders.stream().map(Order::getId).collect(Collectors.toSet());
    }
}
