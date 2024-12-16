package pe.idat.ProyectoFinal.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.ProyectoFinal.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
}
