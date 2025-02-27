package org.leon.astonhome.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class PlayerDTO {
    private Integer playerId;
    private String name;
    private String email;
    private String registrationDate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDTO that = (PlayerDTO) o;
        return Objects.equals(playerId, that.playerId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(registrationDate, that.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, name, email, registrationDate);
    }
}
