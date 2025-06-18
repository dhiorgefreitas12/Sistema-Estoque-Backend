package br.com.controleestoque.Controle.Estoque.service;

import br.com.controleestoque.Controle.Estoque.dto.StockMovementRequest;
import br.com.controleestoque.Controle.Estoque.dto.StockMovementResponse;
import br.com.controleestoque.Controle.Estoque.enums.MovementType;
import br.com.controleestoque.Controle.Estoque.exception.BusinessException;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockMovementServiceTest {

    @InjectMocks
    private StockMovementService stockMovementService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldProcessEntrySuccessfully() {
        Product produto = new Product();
        produto.setId(1L);
        produto.setStockQuantity(5);

        StockMovementRequest request = new StockMovementRequest(
                1L,
                MovementType.ENTRY,
                0.0,
                3
        );

        StockMovement movimentoSalvo = new StockMovement();
        movimentoSalvo.setId(10L);
        movimentoSalvo.setProduct(produto);
        movimentoSalvo.setMovementType(MovementType.ENTRY);
        movimentoSalvo.setSalePrice(0.0);
        movimentoSalvo.setSaleDate(LocalDateTime.now());
        movimentoSalvo.setQuantityMoved(3);

        when(productRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(movimentoSalvo);

        StockMovementResponse response = stockMovementService.processMovement(request);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals(MovementType.ENTRY, response.movementType());
        verify(productRepository, times(1)).save(produto);
    }

    @Test
    void shouldProcessSaidSuccessfullyWhenHasStock() {
        Product produto = new Product();
        produto.setId(1L);
        produto.setStockQuantity(10);

        StockMovementRequest request = new StockMovementRequest(
                1L,
                MovementType.SAID,
                3000.0,
                5
        );

        StockMovement movimentoSalvo = new StockMovement();
        movimentoSalvo.setId(20L);
        movimentoSalvo.setProduct(produto);
        movimentoSalvo.setMovementType(MovementType.SAID);
        movimentoSalvo.setSalePrice(3000.0);
        movimentoSalvo.setSaleDate(LocalDateTime.now());
        movimentoSalvo.setQuantityMoved(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(movimentoSalvo);

        StockMovementResponse response = stockMovementService.processMovement(request);

        assertNotNull(response);
        assertEquals(20L, response.id());
        assertEquals(MovementType.SAID, response.movementType());
        assertEquals(5, produto.getStockQuantity()); // 10 - 5
        verify(productRepository, times(1)).save(produto);
    }

    @Test
    void shouldThrowBusinessExceptionWhenStockIsInsufficient() {
        Product produto = new Product();
        produto.setId(1L);
        produto.setStockQuantity(2);

        StockMovementRequest request = new StockMovementRequest(
                1L,
                MovementType.SAID,
                3000.0,
                5
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(produto));

        assertThrows(BusinessException.class, () -> stockMovementService.processMovement(request));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenProductDoesNotExist() {
        StockMovementRequest request = new StockMovementRequest(
                99L,
                MovementType.ENTRY,
                0.0,
                3
        );

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> stockMovementService.processMovement(request));
    }
}
