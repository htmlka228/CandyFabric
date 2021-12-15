package org.vaadin.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Supplier;
import org.vaadin.example.repository.SupplierRepository;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElse(null);
    }
}
