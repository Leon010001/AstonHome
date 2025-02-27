package org.leon.astonhome.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leon.astonhome.dto.DeveloperDTO;
import org.leon.astonhome.entity.Developer;
import org.leon.astonhome.mapper.DeveloperMapper;
import org.leon.astonhome.repository.DeveloperRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeveloperServiceTest {
    private DeveloperRepository repositoryMock;
    private DeveloperMapper mapperMock;
    private DeveloperService service;

    @BeforeEach
    void setUp() {
        repositoryMock = mock(DeveloperRepository.class);
        mapperMock = mock(DeveloperMapper.class);
        service = new DeveloperService(repositoryMock, mapperMock);
    }

    @Test
    void testCreateDeveloper() {
        DeveloperDTO inputDto = new DeveloperDTO();
        inputDto.setName("New Developer");
        inputDto.setFoundationYear(2020);

        Developer entity = new Developer();
        entity.setName("New Developer");
        entity.setFoundationYear(2020);

        Developer createdEntity = new Developer();
        createdEntity.setDeveloperId(1);
        createdEntity.setName("New Developer");
        createdEntity.setFoundationYear(2020);

        DeveloperDTO outputDto = new DeveloperDTO();
        outputDto.setDeveloperId(1);
        outputDto.setName("New Developer");
        outputDto.setFoundationYear(2020);

        when(mapperMock.toEntity(inputDto)).thenReturn(entity);
        when(repositoryMock.create(entity)).thenReturn(createdEntity);
        when(mapperMock.toDto(createdEntity)).thenReturn(outputDto);

        DeveloperDTO result = service.createDeveloper(inputDto);
        assertNotNull(result);
        assertEquals(1, result.getDeveloperId());
        assertEquals("New Developer", result.getName());
        assertEquals(2020, result.getFoundationYear());
    }

    @Test
    void testUpdateDeveloper() {
        int id = 1;
        DeveloperDTO inputDto = new DeveloperDTO();
        inputDto.setName("Updated Developer");

        Developer developerEntity = new Developer();
        developerEntity.setName("Updated Developer");

        Developer updatedEntity = new Developer();
        updatedEntity.setDeveloperId(id);
        updatedEntity.setName("Updated Developer");

        when(mapperMock.toEntity(inputDto)).thenReturn(developerEntity);
        when(repositoryMock.update(any(Developer.class))).thenReturn(true);

        boolean result = service.updateDeveloper(id, inputDto);
        assertTrue(result);

        verify(mapperMock).toEntity(inputDto);
        verify(repositoryMock).update(argThat(developer -> developerEntity.getDeveloperId() == id &&
                "Updated Developer".equals(developer.getName())));
    }

    @Test
    void testGetDeveloperById() {
        int id = 1;
        Developer entity = new Developer();
        entity.setDeveloperId(id);
        entity.setName("Test Developer");
        entity.setFoundationYear(2000);

        DeveloperDTO dto = new DeveloperDTO();
        dto.setDeveloperId(id);
        dto.setName("Test Developer");
        dto.setFoundationYear(2000);

        when(repositoryMock.getById(id)).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(dto);

        DeveloperDTO result = service.getDeveloperById(id);
        assertNotNull(result);
        assertEquals(id, result.getDeveloperId());
        assertEquals("Test Developer", result.getName());
        assertEquals(2000, result.getFoundationYear());
    }

    @Test
    void testDeleteDeveloper() {
        int id = 1;
        when(repositoryMock.delete(id)).thenReturn(true);
        boolean result = service.deleteDeveloper(id);
        assertTrue(result);
        verify(repositoryMock).delete(id);
    }
}