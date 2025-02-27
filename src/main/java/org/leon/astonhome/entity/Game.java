package org.leon.astonhome.entity;

import lombok.Data;

@Data
public class Game {
    private Integer gameId;
    private String title;
    private Integer developerId;
    private Integer releaseYear;
    private String genre;
}
