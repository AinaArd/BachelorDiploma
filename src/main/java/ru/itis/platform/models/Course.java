package ru.itis.platform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "app")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToMany(mappedBy = "courses")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    private List<User> users;

    @OneToOne
    private App app;
}
