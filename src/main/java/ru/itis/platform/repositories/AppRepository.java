package ru.itis.platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.platform.models.App;
import ru.itis.platform.models.Course;

import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {
    Optional<App> findByAppNameAndCourse(String appName, Course course);
}
