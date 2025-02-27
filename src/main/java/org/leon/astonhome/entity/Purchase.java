package org.leon.astonhome.entity;

import lombok.Data;

@Data
public class Purchase {
    private Integer purchaseId;
    private Integer gameId;
    private Integer playerId;
    private String purchaseDate;
}
