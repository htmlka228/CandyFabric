package org.vaadin.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vaadin.example.entity.CandyFabric;
import org.vaadin.example.entity.Supplier;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponentRequest {
    private CandyFabric candyFabric;
    private Integer numberComponents;
    private Supplier supplier;
    private boolean processed;
}
