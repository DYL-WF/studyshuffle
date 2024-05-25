package net.sytes.studyshuffle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sytes.studyshuffle.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserId(String userId);

  Boolean existsByUserId(String userId);

  Boolean existsByEmail(String email);
}
