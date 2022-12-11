package sqb.uz.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sqb.uz.task.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long > {
}
