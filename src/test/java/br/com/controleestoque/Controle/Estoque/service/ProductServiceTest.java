package br.com.controleestoque.Controle.Estoque.service;

import br.com.controleestoque.Controle.Estoque.dto.ProductProfitResponse;
import br.com.controleestoque.Controle.Estoque.dto.ProductReportResponse;
import br.com.controleestoque.Controle.Estoque.dto.ProductRequest;
import br.com.controleestoque.Controle.Estoque.dto.ProductResponse;
import br.com.controleestoque.Controle.Estoque.enums.ProductType;
import br.com.controleestoque.Controle.Estoque.exception.NotFoundException;
import br.com.controleestoque.Controle.Estoque.model.Product;
import br.com.controleestoque.Controle.Estoque.model.StockMovement;
import br.com.controleestoque.Controle.Estoque.repository.ProductRepository;
import br.com.controleestoque.Controle.Estoque.repository.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        ProductRequest request = new ProductRequest(
                "TV123",
                "Televisão 55 polegadas",
                ProductType.ELETRONICO,
                2500.0,
                10
        );

        Product produtoSalvo = new Product();
        produtoSalvo.setId(1L);
        produtoSalvo.setCode(request.code());
        produtoSalvo.setDescription(request.description());
        produtoSalvo.setProductType(request.productType());
        produtoSalvo.setSupplierPrice(request.supplierPrice());
        produtoSalvo.setStockQuantity(request.stockQuantity());

        when(productRepository.save(any(Product.class))).thenReturn(produtoSalvo);

        ProductResponse response = productService.create(request);

        assertNotNull(response);
        assertEquals("TV123", response.code());
        assertEquals(1L, response.id());
    }

    @Test
    void shouldReturnAllProducts() {
        Product produto = new Product();
        produto.setId(1L);
        produto.setCode("TV123");
        produto.setDescription("Televisão");
        produto.setProductType(ProductType.ELETRONICO);
        produto.setSupplierPrice(2500.0);
        produto.setStockQuantity(10);

        when(productRepository.findAll()).thenReturn(List.of(produto));

        List<ProductResponse> result = productService.getAll();

        assertEquals(1, result.size());
        assertEquals("TV123", result.get(0).code());
    }

    @Test
    void shouldUpdateProductWhenExists() {
        Long produtoId = 1L;
        Product produtoExistente = new Product();
        produtoExistente.setId(produtoId);
        produtoExistente.setCode("TV123");
        produtoExistente.setDescription("Televisão antiga");
        produtoExistente.setProductType(ProductType.ELETRONICO);
        produtoExistente.setSupplierPrice(2000.0);
        produtoExistente.setStockQuantity(5);

        ProductRequest request = new ProductRequest(
                "TV123",
                "Televisão nova",
                ProductType.ELETRODOMESTICO,
                2700.0,
                8
        );

        when(productRepository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));
        when(productRepository.save(any(Product.class))).thenReturn(produtoExistente);

        ProductResponse updated = productService.update(produtoId, request);

        assertEquals("TV123", updated.code());
        assertEquals(ProductType.ELETRODOMESTICO, updated.productType());
        assertEquals("Televisão nova", updated.description());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdateProductDoesNotExist() {
        Long produtoId = 99L;

        when(productRepository.findById(produtoId)).thenReturn(Optional.empty());

        ProductRequest request = new ProductRequest(
                "TV123",
                "Televisão",
                ProductType.ELETRONICO,
                2500.0,
                10
        );

        assertThrows(NotFoundException.class, () -> productService.update(produtoId, request));
    }

    @Test
    void shouldDeleteProduct() {
        Long produtoId = 1L;

        productService.delete(produtoId);

        verify(productRepository, times(1)).deleteById(produtoId);
    }

    @Test
    void shouldFindProductsByType() {
        Product produto = new Product();
        produto.setId(1L);
        produto.setProductType(ProductType.ELETRONICO);

        when(productRepository.findByProductType(ProductType.ELETRONICO)).thenReturn(List.of(produto));

        List<ProductResponse> result = productService.findByType(ProductType.ELETRONICO);

        assertEquals(1, result.size());
        assertEquals(ProductType.ELETRONICO, result.get(0).productType());
    }

    @Test
    void shouldReturnProductReportByType() {
        Product produto = new Product();
        produto.setId(1L);
        produto.setProductType(ProductType.ELETRONICO);
        produto.setCode("TV123");
        produto.setDescription("Televisão");
        produto.setStockQuantity(10);

        StockMovement movimentoSaida = new StockMovement();
        movimentoSaida.setMovementType(br.com.controleestoque.Controle.Estoque.enums.MovementType.SAID);
        movimentoSaida.setQuantityMoved(3);

        when(productRepository.findByProductType(ProductType.ELETRONICO)).thenReturn(List.of(produto));
        when(stockMovementRepository.findByProductId(1L)).thenReturn(List.of(movimentoSaida));

        List<ProductReportResponse> report = productService.findByTypeWithReport(ProductType.ELETRONICO);

        assertEquals(1, report.size());
        assertEquals(3, report.get(0).totalOutputQuantity());
        assertEquals(10, report.get(0).availableQuantity());
    }

    @Test
    void shouldReturnProfitForProduct() {
        Product produto = new Product();
        produto.setId(1L);
        produto.setSupplierPrice(2500.0);
        produto.setCode("TV123");

        StockMovement movimentoSaida = new StockMovement();
        movimentoSaida.setMovementType(br.com.controleestoque.Controle.Estoque.enums.MovementType.SAID);
        movimentoSaida.setQuantityMoved(2);
        movimentoSaida.setSalePrice(3500.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(stockMovementRepository.findByProductId(1L)).thenReturn(List.of(movimentoSaida));

        ProductProfitResponse profit = productService.getProfit(1L);

        assertEquals(2, profit.totalOutputQuantity());
        assertEquals(7000.0, profit.totalSaleValue());
        assertEquals(5000.0, profit.totalCost());
        assertEquals(2000.0, profit.profit());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenProfitProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProfit(1L));
    }
}

