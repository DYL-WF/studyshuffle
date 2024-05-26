package net.sytes.studyshuffle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.sytes.studyshuffle.models.CourseModule;

public interface ModuleRepository extends JpaRepository<CourseModule, Long>{
}
