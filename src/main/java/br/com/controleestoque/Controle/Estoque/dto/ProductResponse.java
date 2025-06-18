package br.com.controleestoque.Controle.Estoque.dto;

import br.com.controleestoque.Controle.Estoque.enums.ProductType;

public record ProductResponse(
        Long id,
        String code,
        String description,
        ProductType productType,
        Double supplierPrice,
        Integer stockQuantity
) {
}
