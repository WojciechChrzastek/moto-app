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
  public ResponseEntity<User> addUser(@RequestBody User newUser) {
    return validateCreateResponse(newUser);
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
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(users, HttpStatus.OK);
    } catch (
            Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<Optional<User>> getUserById(@PathVariable long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(optionalUser);
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUserById(
          @RequestBody User inputtedUser,
          @PathVariable long id) {
    return validateUpdateResponse(inputtedUser, id);
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<User> deleteUserById(@PathVariable long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    userRepository.delete(optionalUser.get());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/users")
  public ResponseEntity<User> deleteAllUsers() {
    if (userRepository.findAll().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    userRepository.deleteAll();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<User> login(@RequestBody User user) {
    List<User> userFromDb = userRepository.findByUsername(user.getUsername());
    if (user.getUsername().equals("") || user.getPassword().equals("")) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    } else if (userFromDb.isEmpty() || hasWrongPassword(userFromDb.get(0), user)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok().build();
  }

  private boolean hasWrongPassword(User userFromDb, User user) {
    return !userFromDb.getPassword().equals(user.getPassword());
  }

  private ResponseEntity<User> validateCreateResponse(@RequestBody User newUser) {
    if (newUser.getUsername().equals("") || newUser.getEmail().equals("") || newUser.getPassword().equals("")) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    } else if (!newUser.getEmail().matches("[^@]+@[^\\.]+\\..+")) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    } else if (!userRepository.findByUsername(newUser.getUsername()).isEmpty()
            || !userRepository.findByEmail(newUser.getEmail()).isEmpty()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } else {
      userRepository.save(newUser);
      return ResponseEntity.ok(newUser);
    }
  }

  private ResponseEntity<User> validateUpdateResponse(User inputtedUser, long id) {
    User existingUser = userRepository.findExistingUserById(id);
    Optional<User> optionalUser = userRepository.findById(id);
    if (inputtedUser.getUsername().equals("") || inputtedUser.getEmail().equals("") || inputtedUser.getPassword().equals("")) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    } else if (!inputtedUser.getEmail().matches("[^@]+@[^\\.]+\\..+")) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    } else if (optionalUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } else if (
            (
                    !(userRepository.findByUsername(inputtedUser.getUsername()).isEmpty())
                            && (!inputtedUser.getUsername().equals(existingUser.getUsername()))
            ) || (
                    !(userRepository.findByEmail(inputtedUser.getEmail()).isEmpty())
                            && (!inputtedUser.getEmail().equals(existingUser.getEmail()))
            )
    )
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    else {
      userRepository.save(inputtedUser);
      return ResponseEntity.ok(inputtedUser);
    }
  }

}
