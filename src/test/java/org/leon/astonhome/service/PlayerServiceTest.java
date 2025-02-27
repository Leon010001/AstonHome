package org.leon.astonhome.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leon.astonhome.dto.PlayerDTO;
import org.leon.astonhome.entity.Player;
import org.leon.astonhome.mapper.PlayerMapper;
import org.leon.astonhome.repository.PlayerRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {
    private PlayerRepository repositoryMock;
    private PlayerMapper mapperMock;
    private PlayerService service;

    @BeforeEach
    void setUp() {
        repositoryMock = mock(PlayerRepository.class);
        mapperMock = mock(PlayerMapper.class);
        service = new PlayerService(repositoryMock, mapperMock);
    }

    @Test
    void testCreatePlayer() {
        PlayerDTO inputDto = new PlayerDTO();
        inputDto.setName("New Player");
        inputDto.setEmail("newPlayer@example.com");

        Player entity = new Player();
        entity.setName("New Player");
        entity.setEmail("newPlayer@example.com");

        Player createdEntity = new Player();
        createdEntity.setPlayerId(1);
        createdEntity.setName("New Player");
        createdEntity.setEmail("newPlayer@example.com");

        PlayerDTO outputDto = new PlayerDTO();
        outputDto.setPlayerId(1);
        outputDto.setName("New Player");
        outputDto.setEmail("newPlayer@example.com");

        when(mapperMock.toEntity(inputDto)).thenReturn(entity);
        when(repositoryMock.create(entity)).thenReturn(createdEntity);
        when(mapperMock.toDto(createdEntity)).thenReturn(outputDto);

        PlayerDTO result = service.createPlayer(inputDto);
        assertNotNull(result);
        assertEquals(1, result.getPlayerId());
        assertEquals("New Player", result.getName());

        verify(mapperMock).toEntity(inputDto);
        verify(repositoryMock).create(entity);
        verify(mapperMock).toDto(createdEntity);
    }

    @Test
    void testUpdatePlayer() {
        int id = 1;
        PlayerDTO inputDto = new PlayerDTO();
        inputDto.setName("Updated Player");

        Player developerEntity = new Player();
        developerEntity.setName("Updated Player");

        Player updatedEntity = new Player();
        updatedEntity.setPlayerId(id);
        updatedEntity.setName("Updated Player");

        when(mapperMock.toEntity(inputDto)).thenReturn(developerEntity);
        when(repositoryMock.update(any(Player.class))).thenReturn(true);

        boolean result = service.updatePlayer(id, inputDto);
        assertTrue(result);

        verify(mapperMock).toEntity(inputDto);
        verify(repositoryMock).update(argThat(player -> updatedEntity.getPlayerId() == id &&
                "Updated Player".equals(player.getName())));
    }

    @Test
    void testGetPlayerById() {
        int id = 1;
        Player entity = new Player();
        entity.setPlayerId(id);
        entity.setName("Test Player");

        PlayerDTO dto = new PlayerDTO();
        dto.setPlayerId(id);
        dto.setName("Test Player");

        when(repositoryMock.getById(id)).thenReturn(entity);
        when(mapperMock.toDto(entity)).thenReturn(dto);

        PlayerDTO result = service.getPlayerById(id);
        assertNotNull(result);
        assertEquals(id, result.getPlayerId());
        assertEquals("Test Player", result.getName());

        verify(repositoryMock).getById(id);
        verify(mapperMock).toDto(entity);
    }

    @Test
    void testDeletePlayer() {
        int id = 1;
        when(repositoryMock.delete(id)).thenReturn(true);
        boolean result = service.deletePlayer(id);
        assertTrue(result);
        verify(repositoryMock).delete(id);
    }
}