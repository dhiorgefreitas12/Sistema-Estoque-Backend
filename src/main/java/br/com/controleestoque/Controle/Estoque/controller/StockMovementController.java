package br.com.controleestoque.Controle.Estoque.controller;

import br.com.controleestoque.Controle.Estoque.dto.StockMovementRequest;
import br.com.controleestoque.Controle.Estoque.dto.StockMovementResponse;
import br.com.controleestoque.Controle.Estoque.service.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movements")
@Tag(
        name = "Movimentações de Estoque",
        description = "Endpoints para registrar entradas e saídas de estoque de produtos."
)
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    @Operation(
            summary = "Registrar uma movimentação de estoque",
            description = "Cria uma movimentação de estoque para um produto. Use o tipo ENTRADA para adicionar itens ao estoque ou SAIDA para registrar uma venda e reduzir a quantidade em estoque."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimentação processada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou estoque insuficiente"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public StockMovementResponse processMovement(@Valid @RequestBody StockMovementRequest request) {
        return stockMovementService.processMovement(request);
    }
}
