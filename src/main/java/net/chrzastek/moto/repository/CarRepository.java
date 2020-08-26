package net.chrzastek.moto.repository;

import net.chrzastek.moto.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
  Optional<Car> findById(long id);
  List<Car> findByBrandnameContaining(String brandname);
  List<Car> findByModelnameContaining(String modelname);
  List<Car> findByBrandname(String brandname);
  List<Car> findByModelname(String modelname);

}
