package br.com.controleestoque.Controle.Estoque.controller;

import br.com.controleestoque.Controle.Estoque.dto.ProductProfitResponse;
import br.com.controleestoque.Controle.Estoque.dto.ProductReportResponse;
import br.com.controleestoque.Controle.Estoque.dto.ProductRequest;
import br.com.controleestoque.Controle.Estoque.dto.ProductResponse;
import br.com.controleestoque.Controle.Estoque.enums.ProductType;
import br.com.controleestoque.Controle.Estoque.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(
        name = "Produtos",
        description = "API para cadastro, atualização, consulta, remoção e relatórios de produtos."
)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(
            summary = "Criar um novo produto",
            description = "Cadastra um novo produto no sistema, incluindo tipo, descrição, preço de fornecedor e estoque inicial."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma lista de todos os produtos cadastrados no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar um produto",
            description = "Atualiza os dados de um produto existente pelo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar um produto",
            description = "Remove um produto do sistema pelo ID informado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/type/{productType}")
    @Operation(
            summary = "Buscar produtos por tipo",
            description = "Retorna uma lista de produtos filtrando pelo tipo especificado (exemplo: ELETRONICO)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista filtrada retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo inválido")
    })
    public List<ProductResponse> findByType(@PathVariable ProductType productType) {
        return productService.findByType(productType);
    }

    @GetMapping("/type/{productType}/report")
    @Operation(
            summary = "Relatório de produtos por tipo",
            description = "Gera um relatório com quantidade de saídas e saldo atual dos produtos de um determinado tipo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo inválido")
    })
    public List<ProductReportResponse> findByTypeWithReport(@PathVariable ProductType productType) {
        return productService.findByTypeWithReport(productType);
    }

    @GetMapping("/profit/{codeProduct}")
    @Operation(
            summary = "Consultar lucro de um produto",
            description = "Calcula o lucro bruto de um produto específico, considerando todas as saídas registradas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lucro calculado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ProductProfitResponse getProfit(@PathVariable String codeProduct) {
        return productService.getProfit(codeProduct);
    }
}
