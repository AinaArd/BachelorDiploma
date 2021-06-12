package ru.itis.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.platform.models.Documentation;

import java.util.Optional;

@Repository
public interface DocumentationRepository extends JpaRepository<Documentation, Long> {
    Optional<Documentation> findByClassName(String className);
}
