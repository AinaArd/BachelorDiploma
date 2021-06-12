package ru.itis.platform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "course")
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appName;
    private LocalDateTime creationDate;
    private String projectPath;
    private Status status;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    private User user;

    @OneToOne
    private Course course;
}
