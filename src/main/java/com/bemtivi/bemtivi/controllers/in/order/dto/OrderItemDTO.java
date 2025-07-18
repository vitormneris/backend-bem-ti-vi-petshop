package com.bemtivi.bemtivi.controllers.in.order.dto;

import com.bemtivi.bemtivi.controllers.in.product.dto.ProductDTO;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemDTO(
        String id,
        BigDecimal price,
        @NotNull(groups = {OrderDTO.OnCreate.class}, message = "A quantidade deve ser preenchida.")
        Integer quantity,
        @NotNull(groups = {OrderDTO.OnCreate.class}, message = "A produto deve ser preenchido.")
        ProductDTO product
) {

}
