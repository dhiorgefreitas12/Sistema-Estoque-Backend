package br.com.controleestoque.Controle.Estoque.repository;

import br.com.controleestoque.Controle.Estoque.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByProductId(Long productId);

}
