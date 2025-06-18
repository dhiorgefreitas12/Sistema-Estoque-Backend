package br.com.controleestoque.Controle.Estoque.model;

import br.com.controleestoque.Controle.Estoque.enums.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 20, unique = true, nullable = false)
    private String code;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductType productType;

    @NotNull
    @PositiveOrZero
    private Double supplierPrice;

    @NotNull
    @Min(0)
    private Integer stockQuantity;
}


