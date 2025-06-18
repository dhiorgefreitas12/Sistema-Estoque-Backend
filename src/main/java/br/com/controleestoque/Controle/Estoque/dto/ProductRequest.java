package br.com.controleestoque.Controle.Estoque.dto;

import br.com.controleestoque.Controle.Estoque.enums.ProductType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequest(
        @NotBlank String code,
        @NotBlank String description,
        @NotNull ProductType productType,
        @NotNull @PositiveOrZero Double supplierPrice,
        @NotNull @Min(0) Integer stockQuantity
) {
}
