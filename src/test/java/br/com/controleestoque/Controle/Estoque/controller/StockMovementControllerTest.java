package br.com.controleestoque.Controle.Estoque.controller;

import br.com.controleestoque.Controle.Estoque.dto.StockMovementRequest;
import br.com.controleestoque.Controle.Estoque.dto.StockMovementResponse;
import br.com.controleestoque.Controle.Estoque.enums.MovementType;
import br.com.controleestoque.Controle.Estoque.service.StockMovementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StockMovementControllerTest {

    private MockMvc mockMvc;
    private StockMovementService stockMovementService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        stockMovementService = mock(StockMovementService.class);
        StockMovementController controller = new StockMovementController(stockMovementService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldProcessEntradyMovement() throws Exception {
        StockMovementRequest request = new StockMovementRequest(
                1L,
                MovementType.ENTRY,
                0.0,
                5
        );

        StockMovementResponse response = new StockMovementResponse(
                1L,
                request.productId(),
                request.movementType(),
                request.salePrice(),
                "2025-06-17T12:00:00",
                request.quantityMoved()
        );

        Mockito.when(stockMovementService.processMovement(any(StockMovementRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.movementType").value("ENTRY"))
                .andExpect(jsonPath("$.quantityMoved").value(5));
    }

    @Test
    void shouldProcessSaidMovement() throws Exception {
        StockMovementRequest request = new StockMovementRequest(
                1L,
                MovementType.SAID,
                3500.0,
                2
        );

        StockMovementResponse response = new StockMovementResponse(
                2L,
                request.productId(),
                request.movementType(),
                request.salePrice(),
                "2025-06-17T12:30:00",
                request.quantityMoved()
        );

        Mockito.when(stockMovementService.processMovement(any(StockMovementRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.movementType").value("SAID"))
                .andExpect(jsonPath("$.salePrice").value(3500.0))
                .andExpect(jsonPath("$.quantityMoved").value(2));
    }
}
