package org.leon.astonhome.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leon.astonhome.dto.GameDTO;
import org.leon.astonhome.entity.Game;
import org.leon.astonhome.mapper.GameMapper;
import org.leon.astonhome.repository.GameRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private GameRepository gameRepositoryMock;
    private GameMapper gameMapperMock;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameRepositoryMock = mock(GameRepository.class);
        gameMapperMock = mock(GameMapper.class);
        gameService = new GameService(gameRepositoryMock, gameMapperMock);
    }

    @Test
    void testCreateGame() {
        GameDTO inputDto = new GameDTO();
        inputDto.setTitle("New Game");
        inputDto.setDeveloperId(1);
        inputDto.setReleaseYear(2023);
        inputDto.setGenre("RPG");

        Game gameEntity = new Game();
        gameEntity.setTitle("New Game");
        gameEntity.setDeveloperId(1);
        gameEntity.setReleaseYear(2023);
        gameEntity.setGenre("RPG");

        Game createdGame = new Game();
        createdGame.setGameId(1);
        createdGame.setTitle("New Game");
        createdGame.setDeveloperId(1);
        createdGame.setReleaseYear(2023);
        createdGame.setGenre("RPG");

        GameDTO outputDto = new GameDTO();
        outputDto.setGameId(1);
        outputDto.setTitle("New Game");
        outputDto.setDeveloperId(1);
        outputDto.setReleaseYear(2023);
        outputDto.setGenre("RPG");

        when(gameMapperMock.toEntity(inputDto)).thenReturn(gameEntity);
        when(gameRepositoryMock.create(gameEntity)).thenReturn(createdGame);
        when(gameMapperMock.toDto(createdGame)).thenReturn(outputDto);

        GameDTO result = gameService.createGame(inputDto);
        assertNotNull(result);
        assertEquals(1, result.getGameId());
        assertEquals("New Game", result.getTitle());
        verify(gameMapperMock).toEntity(inputDto);
        verify(gameRepositoryMock).create(gameEntity);
        verify(gameMapperMock).toDto(createdGame);
    }

    @Test
    void testGetGameById() {
        int id = 1;
        Game gameEntity = new Game();
        gameEntity.setGameId(id);
        gameEntity.setTitle("Test Game");
        gameEntity.setDeveloperId(1);
        gameEntity.setReleaseYear(2023);
        gameEntity.setGenre("Action");

        GameDTO outputDto = new GameDTO();
        outputDto.setGameId(id);
        outputDto.setTitle("Test Game");
        outputDto.setDeveloperId(1);
        outputDto.setReleaseYear(2023);
        outputDto.setGenre("Action");

        when(gameRepositoryMock.getById(id)).thenReturn(gameEntity);
        when(gameMapperMock.toDto(gameEntity)).thenReturn(outputDto);

        GameDTO result = gameService.getGameById(id);
        assertNotNull(result);
        assertEquals(id, result.getGameId());
        assertEquals("Test Game", result.getTitle());
        verify(gameRepositoryMock).getById(id);
        verify(gameMapperMock).toDto(gameEntity);
    }

    @Test
    void testUpdateGame() {
        int id = 1;
        GameDTO inputDto = new GameDTO();
        inputDto.setGameId(id);
        inputDto.setTitle("Updated Game");
        inputDto.setDeveloperId(2);
        inputDto.setReleaseYear(2023);
        inputDto.setGenre("RPG");

        Game gameEntity = new Game();
        gameEntity.setGameId(id);
        gameEntity.setTitle("Updated Game");
        gameEntity.setDeveloperId(2);
        gameEntity.setReleaseYear(2023);
        gameEntity.setGenre("RPG");

        when(gameMapperMock.toEntity(inputDto)).thenReturn(gameEntity);
        when(gameRepositoryMock.update(gameEntity)).thenReturn(true);

        boolean result = gameService.updateGame(id, inputDto);
        assertTrue(result);
        verify(gameMapperMock).toEntity(inputDto);
        verify(gameRepositoryMock).update(argThat(game -> game.getGameId() == id &&
                "Updated Game".equals(game.getTitle()) &&
                2 == game.getDeveloperId() &&
                2023 == game.getReleaseYear() &&
                "RPG".equals(game.getGenre())));
    }

    @Test
    void testDeleteGame() {
        int id = 1;
        when(gameRepositoryMock.delete(id)).thenReturn(true);
        boolean result = gameService.deleteGame(id);
        assertTrue(result);
        verify(gameRepositoryMock).delete(id);
    }
}