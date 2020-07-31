package net.chrzastek.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.chrzastek.cars.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ObjectMapper objectMapper;

  @GetMapping("/users")
  public ResponseEntity getUsers() throws JsonProcessingException {
    List<User> users = userRepository.findAll();
    return ResponseEntity.ok(objectMapper.writeValueAsString(users));
  }

  @GetMapping("/users/{id}")
  public ResponseEntity getUserById(@PathVariable long id) {
    Optional<User> user = userRepository.findById(id);

    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
    return ResponseEntity.ok(user);
  }

  @PostMapping("/users")
  public ResponseEntity addUser(@RequestBody User user) {
    Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());

    if (userFromDb.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    User savedUser = userRepository.save(user);
    return ResponseEntity.ok(savedUser);
  }

  @PutMapping("/users/{id}")
  public ResponseEntity updateUserById(
          @RequestHeader("username") String username,
          @RequestBody ObjectNode objectNode,
          @PathVariable long id) {
    Optional<User> user = userRepository.findById(id);

    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    User u = userRepository.getOne(id);

    u.setUsername(objectNode.get("username").asText());
    u.setPassword(objectNode.get("password").asText());
    u.setEmail(objectNode.get("email").asText());

    userRepository.save(u);
    return ResponseEntity.ok(u);
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody User user) {
    Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());

    if(userFromDb.isEmpty() || hasWrongPassword(userFromDb, user)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok().build();

  }

  private boolean hasWrongPassword(Optional<User> userFromDb, User user) {
    return !userFromDb.get().getPassword().equals(user.getPassword());
  }

}
