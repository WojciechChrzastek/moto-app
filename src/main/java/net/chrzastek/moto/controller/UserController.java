package net.chrzastek.moto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrzastek.moto.AllowedCors;
import net.chrzastek.moto.entity.User;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {AllowedCors.FRONT1, AllowedCors.FRONT2})
@RestController
public class UserController {
  final UserRepository userRepository;
  final ObjectMapper objectMapper;

  @Autowired
  public UserController(UserRepository userRepository, ObjectMapper objectMapper) {
    this.userRepository = userRepository;
    this.objectMapper = objectMapper;
  }

  @PostMapping("/users")
  public ResponseEntity<User> addUser(@RequestBody User user) {
    return validateResponse(user);
  }

  @GetMapping("/users")
  public ResponseEntity<List<User>> getUsers(
          @RequestParam(required = false) String username,
          @RequestParam(required = false) String email,
          @RequestParam(required = false) String password
  ) {
    try {
      List<User> users = new ArrayList<>();
      if (username == null && email == null && password == null) {
        users.addAll(userRepository.findAll());
      } else if (username == null && email == null) {
        users.addAll(userRepository.findByPasswordContaining(password));
      } else if (username == null && password == null) {
        users.addAll(userRepository.findByEmailContaining(email));
      } else {
        users.addAll(userRepository.findByUsernameContaining(username));
      }
      if (users.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(users, HttpStatus.OK);
    } catch (
            Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/users/{id}")
  public ResponseEntity getUserById(@PathVariable long id) {
    Optional<User> user = userRepository.findById(id);

    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
    return ResponseEntity.ok(user);
  }

//  @GetMapping("/users/name")
//  public ResponseEntity getUserByUsername(@RequestBody ObjectNode objectNode) {
//    Optional<User> user = userRepository.findByUsername(objectNode.get("username").asText());
//
//    if (user.isEmpty()) {
//      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
//    }
//    return ResponseEntity.ok(user);
//  }

//  @GetMapping("/users/email")
//  public ResponseEntity getUserByEmail(@RequestBody ObjectNode objectNode) {
//    Optional<User> user = userRepository.findByEmail(objectNode.get("email").asText());
//
//    if (user.isEmpty()) {
//      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
//    }
//    return ResponseEntity.ok(user);
//  }

  @PutMapping("/users/{id}")
  public ResponseEntity updateUserById(
//          @RequestHeader("username") String username,
          @RequestBody User user,
          @PathVariable long id) {
    Optional<User> userr = userRepository.findById(id);

    if (userr.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

//    User u = userRepository.getOne(id);

//    u.setUsername(objectNode.get("username").asText());
//    u.setPassword(objectNode.get("password").asText());
//    u.setEmail(objectNode.get("email").asText());

    return validateResponse(user);
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity deleteUserById(@PathVariable long id) {

    Optional<User> user = userRepository.findById(id);

    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    User u = userRepository.getOne(id);

    userRepository.delete(u);
    return ResponseEntity.ok(u);
  }

//  @PostMapping("/login")
//  public ResponseEntity login(@RequestBody User user) {
//    Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
//
//    if (userFromDb.isEmpty() || hasWrongPassword(userFromDb, user)) {
//      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//    return ResponseEntity.ok().build();
//
//  }

  private boolean hasWrongPassword(Optional<User> userFromDb, User user) {
    return !userFromDb.get().getPassword().equals(user.getPassword());
  }

  private ResponseEntity<User> validateResponse(@RequestBody User user) {
    if (user.getUsername().equals("") || user.getEmail().equals("") || user.getPassword().equals("")) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    } else if (!userRepository.findByUsername(user.getUsername()).isEmpty()
            || !userRepository.findByEmail(user.getEmail()).isEmpty()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } else {
      userRepository.save(user);
      return ResponseEntity.ok(user);
    }
  }

}
