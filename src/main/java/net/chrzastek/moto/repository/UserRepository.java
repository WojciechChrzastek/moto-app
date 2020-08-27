package net.chrzastek.moto.repository;

import net.chrzastek.moto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findById(long id);
  List<User> findByUsername(String username);
  List<User> findByEmail(String email);
  List<User> findByPassword(String email);
  List<User> findByUsernameContaining(String username);
  List<User> findByEmailContaining(String email);
  List<User> findByPasswordContaining(String password);
}
