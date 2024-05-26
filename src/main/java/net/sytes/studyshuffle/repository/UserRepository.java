package net.sytes.studyshuffle.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sytes.studyshuffle.models.CourseModule;
import net.sytes.studyshuffle.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Set<CourseModule> findCourseModuleByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
