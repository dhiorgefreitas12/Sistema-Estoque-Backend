package br.com.controleestoque.Controle.Estoque.dto;

import br.com.controleestoque.Controle.Estoque.enums.MovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StockMovementRequest(
        @NotNull String productId,
        @NotNull MovementType movementType,
        @NotNull @PositiveOrZero Double salePrice,
        @NotNull @Min(1) Integer quantityMoved
) {
}
