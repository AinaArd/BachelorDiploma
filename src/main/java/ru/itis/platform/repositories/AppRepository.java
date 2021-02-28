package ru.itis.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.platform.models.App;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {
}
