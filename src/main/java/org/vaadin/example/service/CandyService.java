package org.vaadin.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Candy;
import org.vaadin.example.repository.CandyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandyService {
    private final CandyRepository candyRepository;

    public List<Candy> getAllCandies() {
        return candyRepository.findAll();
    }

    public Candy save(Candy candy) {
        return candyRepository.save(candy);
    }
}
