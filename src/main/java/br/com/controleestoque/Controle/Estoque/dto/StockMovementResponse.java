package br.com.controleestoque.Controle.Estoque.dto;

import br.com.controleestoque.Controle.Estoque.enums.MovementType;

public record StockMovementResponse(
        Long id,
        Long productId,
        MovementType movementType,
        Double salePrice,
        String saleDate,
        Integer quantityMoved
) {
}
