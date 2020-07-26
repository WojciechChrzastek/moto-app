package net.chrzastek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

  @PostMapping("/users")
  public ResponseEntity addUser(@RequestBody User user) {
    User savedUser = userRepository.save(user);
    return ResponseEntity.ok(savedUser);
  }
}
