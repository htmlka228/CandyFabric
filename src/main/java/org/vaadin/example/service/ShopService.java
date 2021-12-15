package org.vaadin.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Shop;
import org.vaadin.example.repository.ShopRepository;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public Shop getShopById(Long id) {
        return shopRepository.findById(id)
                .orElse(null);
    }

    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }
}
