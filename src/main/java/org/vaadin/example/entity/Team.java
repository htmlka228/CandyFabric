package org.vaadin.example.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "team")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Achievement> achievements;

    private String name;
    private String country;
    private boolean active;
}
