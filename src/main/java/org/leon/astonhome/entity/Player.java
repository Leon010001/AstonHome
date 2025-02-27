package org.leon.astonhome.entity;

import lombok.Data;

@Data
public class Player {
    private Integer playerId;
    private String name;
    private String email;
    private String registrationDate;
}
