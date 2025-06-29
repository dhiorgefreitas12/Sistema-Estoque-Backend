package br.com.controleestoque.Controle.Estoque.repository;

import br.com.controleestoque.Controle.Estoque.enums.ProductType;
import br.com.controleestoque.Controle.Estoque.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductType(ProductType productType);

    Optional<Product> findByCode(String code);

}
