package org.leon.astonhome.service;

import org.leon.astonhome.dto.PurchaseDTO;
import org.leon.astonhome.entity.Purchase;
import org.leon.astonhome.mapper.PurchaseMapper;
import org.leon.astonhome.repository.PurchaseRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PurchaseService {
    private final PurchaseRepository repository;
    private final PurchaseMapper purchaseMapper;

    public PurchaseService(PurchaseRepository repository, PurchaseMapper purchaseMapper) {
        this.repository = repository;
        this.purchaseMapper = purchaseMapper;
    }

    public PurchaseDTO createPurchase(PurchaseDTO dto) {
        Purchase purchase = purchaseMapper.toEntity(dto);
        Purchase created = repository.create(purchase);
        return purchaseMapper.toDto(created);
    }

    public PurchaseDTO getPurchaseById(int id) {
        Purchase purchase = repository.getById(id);
        return purchaseMapper.toDto(purchase);
    }

    public List<PurchaseDTO> getAllPurchases() {
        List<Purchase> purchases = repository.getAll();
        return purchases.stream().map(purchaseMapper::toDto).collect(Collectors.toList());
    }

    public boolean updatePurchase(int id, PurchaseDTO dto) {
        Purchase purchase = purchaseMapper.toEntity(dto);
        purchase.setPurchaseId(id);
        return repository.update(purchase);
    }

    public boolean deletePurchase(int id) {
        return repository.delete(id);
    }
}
