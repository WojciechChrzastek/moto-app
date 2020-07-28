package net.chrzastek.cars;

import net.chrzastek.users.User;
import net.chrzastek.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CarService {

  @Autowired
  CarRepository carRepository;

  @Autowired
  UserRepository userRepository;

  @PostMapping("/cars")
  public ResponseEntity addCar(
          @RequestHeader("username") String username,
          @RequestBody String brandName,
          @RequestBody String modelName,
          @RequestBody int manufactureYear) {
  Optional<User> userFromDb = userRepository.findByUsername(username);

  if (userFromDb.isEmpty()) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  Car car = new Car(userFromDb.get(), brandName, modelName, manufactureYear);
  Car savedCar = carRepository.save(car);

  return ResponseEntity.ok(savedCar);
  }
}
