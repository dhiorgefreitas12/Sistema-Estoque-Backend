package br.com.controleestoque.Controle.Estoque.controller;

import br.com.controleestoque.Controle.Estoque.dto.ProductProfitResponse;
import br.com.controleestoque.Controle.Estoque.dto.ProductReportResponse;
import br.com.controleestoque.Controle.Estoque.dto.ProductRequest;
import br.com.controleestoque.Controle.Estoque.dto.ProductResponse;
import br.com.controleestoque.Controle.Estoque.enums.ProductType;
import br.com.controleestoque.Controle.Estoque.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    private MockMvc mockMvc;
    private ProductService productService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        ProductController controller = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest(
                "TV123",
                "Televisão 55 polegadas",
                ProductType.ELETRONICO,
                2500.0,
                10
        );

        ProductResponse response = new ProductResponse(
                1L,
                request.code(),
                request.description(),
                request.productType(),
                request.supplierPrice(),
                request.stockQuantity()
        );

        when(productService.create(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("TV123"));

        verify(productService).create(any(ProductRequest.class));
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        ProductResponse produto = new ProductResponse(
                1L, "TV123", "Televisão", ProductType.ELETRONICO, 2500.0, 10
        );

        when(productService.getAll()).thenReturn(List.of(produto));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("TV123"));

        verify(productService).getAll();
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        ProductRequest request = new ProductRequest(
                "TV123",
                "Televisão atualizada",
                ProductType.ELETRODOMESTICO,
                2600.0,
                12
        );

        ProductResponse response = new ProductResponse(
                1L,
                request.code(),
                request.description(),
                request.productType(),
                request.supplierPrice(),
                request.stockQuantity()
        );

        when(productService.update(eq(1L), any(ProductRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Televisão atualizada"));

        verify(productService).update(eq(1L), any(ProductRequest.class));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());

        verify(productService).delete(1L);
    }

    @Test
    void shouldFindProductsByType() throws Exception {
        ProductResponse produto = new ProductResponse(
                1L, "TV123", "Televisão", ProductType.ELETRONICO, 2500.0, 10
        );

        when(productService.findByType(ProductType.ELETRONICO)).thenReturn(List.of(produto));

        mockMvc.perform(get("/products/type/ELETRONICO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productType").value("ELETRONICO"));

        verify(productService).findByType(ProductType.ELETRONICO);
    }

    @Test
    void shouldFindProductsByTypeWithReport() throws Exception {
        ProductReportResponse report = new ProductReportResponse(
                1L, "TV123", "Televisão", "ELETRONICO", 2, 8
        );

        when(productService.findByTypeWithReport(ProductType.ELETRONICO)).thenReturn(List.of(report));

        mockMvc.perform(get("/products/type/ELETRONICO/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalOutputQuantity").value(2))
                .andExpect(jsonPath("$[0].availableQuantity").value(8));

        verify(productService).findByTypeWithReport(ProductType.ELETRONICO);
    }

    @Test
    void shouldGetProductProfit() throws Exception {
        ProductProfitResponse profit = new ProductProfitResponse(
                1L, "TV123", 3, 9000.0, 7500.0, 1500.0
        );

        when(productService.getProfit(1L)).thenReturn(profit);

        mockMvc.perform(get("/products/profit/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profit").value(1500.0));

        verify(productService).getProfit(1L);
    }
}
