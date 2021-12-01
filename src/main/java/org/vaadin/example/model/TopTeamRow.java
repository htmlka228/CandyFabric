package org.vaadin.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopTeamRow {
    private String rank;
    private String teamName;
    private String gameName;
    private String tournamentName;
}
