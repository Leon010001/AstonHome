package org.leon.astonhome.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class PurchaseDTO {
    private Integer purchaseId;
    private Integer gameId;
    private Integer playerId;
    private String purchaseDate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseDTO that = (PurchaseDTO) o;
        return Objects.equals(purchaseId, that.purchaseId) &&
                Objects.equals(gameId, that.gameId) &&
                Objects.equals(playerId, that.playerId) &&
                Objects.equals(purchaseDate, that.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseId, gameId, playerId, purchaseDate);
    }
}
