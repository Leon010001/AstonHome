package org.leon.astonhome.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class DeveloperDTO {
    private Integer developerId;
    private String name;
    private Integer foundationYear;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeveloperDTO that = (DeveloperDTO) o;
        return Objects.equals(developerId, that.developerId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(foundationYear, that.foundationYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(developerId, name, foundationYear);
    }
}
