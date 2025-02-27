package org.leon.astonhome.mapper;

import org.leon.astonhome.dto.PurchaseDTO;
import org.leon.astonhome.entity.Purchase;

public class PurchaseMapper implements Mapper<Purchase, PurchaseDTO> {
    @Override
    public PurchaseDTO toDto(Purchase purchase) {
        if (purchase == null) return null;
        PurchaseDTO dto = new PurchaseDTO();
        dto.setPurchaseId(purchase.getPurchaseId());
        dto.setGameId(purchase.getGameId());
        dto.setPlayerId(purchase.getPlayerId());
        dto.setPurchaseDate(purchase.getPurchaseDate());
        return dto;
    }

    @Override
    public Purchase toEntity(PurchaseDTO dto) {
        if (dto == null) return null;
        Purchase purchase = new Purchase();
        purchase.setPurchaseId(dto.getPurchaseId());
        purchase.setGameId(dto.getGameId());
        purchase.setPlayerId(dto.getPlayerId());
        purchase.setPurchaseDate(dto.getPurchaseDate());
        return purchase;
    }
}
