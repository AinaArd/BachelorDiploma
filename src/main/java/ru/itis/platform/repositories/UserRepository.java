package ru.itis.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.platform.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
