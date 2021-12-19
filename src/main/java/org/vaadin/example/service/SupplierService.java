package org.vaadin.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Supplier;
import org.vaadin.example.repository.SupplierRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElse(null);
    }

    public Supplier getSupplierByName(String name) {
        return supplierRepository.findByName(name)
                .orElse(null);
    }

    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }
}
