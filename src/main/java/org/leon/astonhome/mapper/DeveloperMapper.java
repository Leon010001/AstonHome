package org.leon.astonhome.mapper;

import org.leon.astonhome.dto.DeveloperDTO;
import org.leon.astonhome.entity.Developer;

public class DeveloperMapper implements Mapper<Developer, DeveloperDTO > {

    @Override
    public DeveloperDTO toDto(Developer developer) {
        if (developer == null) return null;
        DeveloperDTO dto = new DeveloperDTO();
        dto.setDeveloperId(developer.getDeveloperId());
        dto.setName(developer.getName());
        dto.setFoundationYear(developer.getFoundationYear());
        return dto;
    }

    @Override
    public Developer toEntity(DeveloperDTO dto) {
        if (dto == null) return null;
        Developer developer = new Developer();
        developer.setDeveloperId(dto.getDeveloperId());
        developer.setName(dto.getName());
        developer.setFoundationYear(dto.getFoundationYear());
        return developer;
    }
}
