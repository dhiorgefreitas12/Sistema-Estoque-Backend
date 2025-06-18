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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StockMovementService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public StockMovementService(ProductRepository productRepository, StockMovementRepository stockMovementRepository) {
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional
    public StockMovementResponse processMovement(StockMovementRequest request) {
        Product product = productRepository.findByCode(request.productId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (request.movementType() == MovementType.SAID && product.getStockQuantity() < request.quantityMoved()) {
            throw new BusinessException("Insufficient stock for output movement");
        }

        if (request.movementType() == MovementType.SAID) {
            product.setStockQuantity(product.getStockQuantity() - request.quantityMoved());
        } else {
            product.setStockQuantity(product.getStockQuantity() + request.quantityMoved());
        }

        productRepository.save(product);

        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setMovementType(request.movementType());
        movement.setSalePrice(request.salePrice());
        movement.setSaleDate(LocalDateTime.now());
        movement.setQuantityMoved(request.quantityMoved());

        StockMovement saved = stockMovementRepository.save(movement);

        return new StockMovementResponse(
                saved.getId(),
                saved.getProduct().getId(),
                saved.getMovementType(),
                saved.getSalePrice(),
                saved.getSaleDate().toString(),
                saved.getQuantityMoved()
        );
    }
}
