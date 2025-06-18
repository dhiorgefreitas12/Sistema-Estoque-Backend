package br.com.controleestoque.Controle.Estoque.dto;

public record ProductProfitResponse(
        Long productId,
        String code,
        Integer totalOutputQuantity,
        Double totalSaleValue,
        Double totalCost,
        Double profit
) {
}
