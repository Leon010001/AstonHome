package org.leon.astonhome.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class GameDTO {
    private Integer gameId;
    private String title;
    private Integer developerId;
    private Integer releaseYear;
    private String genre;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameDTO that = (GameDTO) o;
        return Objects.equals(gameId, that.gameId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(developerId, that.developerId) &&
                Objects.equals(releaseYear, that.releaseYear) &&
                Objects.equals(genre, that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, title, developerId, releaseYear, genre);
    }
}
