package br.com.controleestoque.Controle.Estoque.dto;

public record ProductReportResponse(
        Long id,
        String code,
        String description,
        String productType,
        Integer totalOutputQuantity,
        Integer availableQuantity
) {
}
