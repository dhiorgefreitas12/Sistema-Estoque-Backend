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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;


    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        product.setCode(request.code());
        product.setDescription(request.description());
        product.setProductType(request.productType());
        product.setSupplierPrice(request.supplierPrice());
        product.setStockQuantity(request.stockQuantity());
        product = productRepository.save(product);

        return toResponse(product);
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setCode(request.code());
        product.setDescription(request.description());
        product.setProductType(request.productType());
        product.setSupplierPrice(request.supplierPrice());
        product.setStockQuantity(request.stockQuantity());

        return toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductResponse> findByType(ProductType productType) {
        return productRepository.findByProductType(productType).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCode(),
                product.getDescription(),
                product.getProductType(),
                product.getSupplierPrice(),
                product.getStockQuantity()
        );
    }

    public List<ProductReportResponse> findByTypeWithReport(ProductType type) {
        List<Product> products = productRepository.findByProductType(type);
        return products.stream().map(product -> {
            int totalOutputQuantity = stockMovementRepository
                    .findByProductId(product.getId())
                    .stream()
                    .filter(m -> m.getMovementType().name().equals("SAID"))
                    .mapToInt(StockMovement::getQuantityMoved)
                    .sum();
            return new ProductReportResponse(
                    product.getId(),
                    product.getCode(),
                    product.getDescription(),
                    product.getProductType().name(),
                    totalOutputQuantity,
                    product.getStockQuantity()
            );
        }).collect(Collectors.toList());
    }

    public ProductProfitResponse getProfit(String codeProduct) {
        Product product = productRepository.findByCode(codeProduct)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        List<StockMovement> movements = stockMovementRepository.findByProductId(product.getId());

        int totalOutputQuantity = movements.stream()
                .filter(m -> m.getMovementType().name().equals("SAID"))
                .mapToInt(StockMovement::getQuantityMoved)
                .sum();

        double totalSaleValue = movements.stream()
                .filter(m -> m.getMovementType().name().equals("SAID"))
                .mapToDouble(m -> m.getSalePrice() * m.getQuantityMoved())
                .sum();

        double totalCost = product.getSupplierPrice() * totalOutputQuantity;
        double profit = totalSaleValue - totalCost;

        return new ProductProfitResponse(
                product.getId(),
                product.getCode(),
                totalOutputQuantity,
                totalSaleValue,
                totalCost,
                profit
        );
    }
}
