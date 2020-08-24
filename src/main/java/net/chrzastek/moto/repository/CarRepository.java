package net.chrzastek.moto.repository;

import net.chrzastek.moto.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
  List<Car> findByBrandnameContaining(String brandname);
  List<Car> findById(String id);
}
