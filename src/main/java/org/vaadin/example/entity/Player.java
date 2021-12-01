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
@Table(name = "player")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Game game;

    @ManyToOne
    @JoinColumn
    private Team team;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Team> previousTeams;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Tournament> previousTournaments;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Achievement> achievements;

    private String name;
    private String primaryCountry;
    private boolean active;
}
