package br.com.controleestoque.Controle.Estoque.model;

import br.com.controleestoque.Controle.Estoque.enums.MovementType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MovementType movementType;

    @NotNull
    @PositiveOrZero
    private Double salePrice;

    @NotNull
    private LocalDateTime saleDate;

    @NotNull
    @Min(1)
    private Integer quantityMoved;
}
