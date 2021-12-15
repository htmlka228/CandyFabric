package org.vaadin.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.CandyFabric;
import org.vaadin.example.repository.CandyFabricRepository;

@Service
@RequiredArgsConstructor
public class CandyFabricService {
    private final CandyFabricRepository candyFabricRepository;

    public CandyFabric save(CandyFabric candyFabric) {
        return candyFabricRepository.save(candyFabric);
    }

    public CandyFabric getCandyFabricById(Long id) {
        return candyFabricRepository.findById(id)
                .orElse(null);
    }
}
