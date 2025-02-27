package org.leon.astonhome.service;

import org.leon.astonhome.dto.DeveloperDTO;
import org.leon.astonhome.entity.Developer;
import org.leon.astonhome.mapper.DeveloperMapper;
import org.leon.astonhome.repository.DeveloperRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DeveloperService {
    private final DeveloperRepository repository;
    private final DeveloperMapper mapper;

    public DeveloperService(DeveloperRepository repository, DeveloperMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public DeveloperDTO createDeveloper(DeveloperDTO dto) {
        Developer developer = mapper.toEntity(dto);
        Developer created = repository.create(developer);
        return mapper.toDto(created);
    }

    public DeveloperDTO getDeveloperById(int id) {
        Developer developer = repository.getById(id);
        return mapper.toDto(developer);
    }

    public List<DeveloperDTO> getAllDevelopers() {
        List<Developer> developers = repository.getAll();
        return developers.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public boolean updateDeveloper(int id, DeveloperDTO dto) {
        Developer developer = mapper.toEntity(dto);
        developer.setDeveloperId(id);
        return repository.update(developer);
    }

    public boolean deleteDeveloper(int id) {
        return repository.delete(id);
    }
}
