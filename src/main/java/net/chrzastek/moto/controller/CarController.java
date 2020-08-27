package net.chrzastek.moto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.chrzastek.moto.AllowedCors;
import net.chrzastek.moto.entity.Car;
import net.chrzastek.moto.entity.User;
import net.chrzastek.moto.repository.CarRepository;
import net.chrzastek.moto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {AllowedCors.FRONT1, AllowedCors.FRONT2})
@RestController
public class CarController {
  final CarRepository carRepository;
  final UserRepository userRepository;
  final ObjectMapper objectMapper;

  @Autowired
  public CarController(CarRepository carRepository, UserRepository userRepository, ObjectMapper objectMapper) {
    this.carRepository = carRepository;
    this.userRepository = userRepository;
    this.objectMapper = objectMapper;
  }

//  @PostMapping("/cars")
//  public ResponseEntity addCar(
//          @RequestHeader("username") String username,
//          @RequestBody ObjectNode objectNode) {
//    Optional<User> userFromDb = userRepository.findByUsername(username);
//
//    if (userFromDb.isEmpty()) {
//      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//
//    Car car = new Car(
//            userFromDb.get(),
//            objectNode.get("brandname").asText(),
//            objectNode.get("modelname").asText(),
//            objectNode.get("manufactureyear").asInt());
//    Car savedCar = carRepository.save(car);
//
//    return ResponseEntity.ok(savedCar);
//  }

  @PostMapping("/carsnouser")
  public ResponseEntity<Car> addCar(
//          @RequestHeader(required = false) String username,
          @RequestBody Car car) {

//    Optional<User> user = userRepository.findByUsername(username);
//    User userToSave = userRepository.getOne(user.orElseThrow().getId());
//    car.setUser(userToSave);

    return validateResponse(car);
  }

  @GetMapping("/cars")
  public ResponseEntity<List<Car>> getCars(
          @RequestParam(required = false) String brandname,
          @RequestParam(required = false) String modelname
  ) {
    try {
      List<Car> cars = new ArrayList<>();

      if (brandname == null && modelname == null) {
        cars.addAll(carRepository.findAll());
      } else if (brandname == null) {
        cars.addAll(carRepository.findByModelnameContaining(modelname));
      } else {
        cars.addAll(carRepository.findByBrandnameContaining(brandname));
      }

      if (cars.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(cars, HttpStatus.OK);
    } catch (
            Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @GetMapping("/cars/{id}")
  public ResponseEntity getCarById(@PathVariable long id) {
    Optional<Car> car = carRepository.findById(id);

    if (car.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
    return ResponseEntity.ok(car);
  }

  @GetMapping("/show-car-by-id")
  public ResponseEntity getCarById(@RequestHeader Long id) {
    Optional<Car> car = carRepository.findById(id);

    if (car.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
    return ResponseEntity.ok(car);
  }

  @PutMapping("/cars/{id}")
  public ResponseEntity<Car> updateCarById(
//          @RequestHeader("username") String username,
          @RequestBody Car car,
          @PathVariable long id) {
    Optional<Car> carr = carRepository.findById(id);

    if (carr.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return validateResponse(car);
  }


  @PutMapping("/cars")
  public ResponseEntity updateCarByIdBody(
          @RequestBody ObjectNode objectNode,
          @PathVariable long id) {
    Optional<Car> car = carRepository.findById(id);

    if (car.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    Car c = carRepository.getOne(id);

    c.setBrandname(objectNode.get("brandname").asText());
    c.setModelname(objectNode.get("modelname").asText());
    c.setManufactureyear(objectNode.get("manufactureyear").asInt());

    carRepository.save(c);
    return ResponseEntity.ok(c);
  }

  @DeleteMapping("/cars/{id}")
  public ResponseEntity deleteCarById(@PathVariable long id) {

    Optional<Car> car = carRepository.findById(id);

    if (car.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    Car c = carRepository.getOne(id);

    carRepository.delete(c);
    return ResponseEntity.ok(c);
  }

  @DeleteMapping("/cars")
  public ResponseEntity deleteCarByIdBody(@RequestBody(required = false) Long id) {

    try {
      if (id == 0) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
      }

      Optional<Car> car = carRepository.findById(id);

      if (car.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
      }

      Car c = carRepository.getOne(id);

      carRepository.delete(c);
      return ResponseEntity.ok(c);
    } catch (NullPointerException e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

  }

  private ResponseEntity<Car> validateResponse(@RequestBody Car car) {
    if (car.getBrandname().equals("") || car.getModelname().equals("") || car.getManufactureyear() == 0) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    } else if (
            car.getManufactureyear() < 1901 || car.getManufactureyear() > 2155
    ) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    } else {
      carRepository.save(car);
      return ResponseEntity.ok(car);
    }
  }

}
