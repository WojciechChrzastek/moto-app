package net.chrzastek.cars;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.chrzastek.users.User;
import net.chrzastek.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CarService {

  @Autowired
  CarRepository carRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ObjectMapper objectMapper;

  @GetMapping("/cars")
  public ResponseEntity getCars() throws JsonProcessingException {
    List<Car> cars = carRepository.findAll();
    return ResponseEntity.ok(objectMapper.writeValueAsString(cars));
  }

  @PostMapping("/cars")
  public ResponseEntity addCar(
          @RequestHeader("username") String username,
          @RequestBody ObjectNode objectNode) {
  Optional<User> userFromDb = userRepository.findByUsername(username);

  if (userFromDb.isEmpty()) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  Car car = new Car(
          userFromDb.get(),
          objectNode.get("brandname").asText(),
          objectNode.get("modelname").asText(),
          objectNode.get("manufactureyear").asInt());
  Car savedCar = carRepository.save(car);

  return ResponseEntity.ok(savedCar);
  }
}
