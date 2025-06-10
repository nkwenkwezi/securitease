package com.example.store.mapper;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductSummaryDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "products", expression = "java(mapProductSummaries(order.getProducts()))")
    OrderDTO orderToOrderDTO(Order order);

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);

    OrderCustomerDTO orderToOrderCustomerDTO(Customer customer);

    default List<ProductSummaryDTO> mapProductSummaries(Set<Product> products) {
        if (products == null) return new ArrayList<>();
        return products.stream().map(this::mapProductToSummary).collect(Collectors.toList());
    }

    default ProductSummaryDTO mapProductToSummary(Product product) {

        ProductSummaryDTO dto = new ProductSummaryDTO();
        dto.setId(product.getId());
        dto.setDescription(product.getDescription());
        return dto;
    }

    @Named("orderToId")
    static Long mapOrderToId(Order order) {
        return order.getId();
    }

    @IterableMapping(qualifiedByName = "orderToId")
    default List<Long> mapOrdersToIds(Set<Order> orders) {
        if (orders == null) return List.of();
        return orders.stream().map(OrderMapper::mapOrderToId).toList();
    }
}
