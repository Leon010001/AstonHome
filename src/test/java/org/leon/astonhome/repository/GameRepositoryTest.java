package org.leon.astonhome.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.leon.astonhome.connect.DBConnection;
import org.leon.astonhome.entity.Game;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameRepositoryTest {

    private GameRepository gameRepository;

    @BeforeAll
    public void setUp() {
        gameRepository = new GameRepository();
    }

    @BeforeEach
    public void clearData() {
        DBConnection.executePreparedStatement("TRUNCATE TABLE games RESTART IDENTITY CASCADE",
                stmt -> {},
                stmt -> stmt.executeUpdate());
    }

    @Test
    public void testCreate() throws SQLException {
        Game game = new Game();
        game.setTitle("Test Game");
        game.setDeveloperId(1);
        game.setReleaseYear(2023);
        game.setGenre("Action");

        Game createdGame = gameRepository.create(game);
        assertNotNull(createdGame, "Созданная игра не должна быть null");
        assertNotNull(createdGame.getGameId(), "У игры должен быть присвоен ID после создания");

        Game foundGame = gameRepository.getById(createdGame.getGameId());
        assertNotNull(foundGame, "Игра должна быть найдена по ID");
        assertEquals("Test Game", foundGame.getTitle());
        assertEquals(1, foundGame.getDeveloperId());
        assertEquals(2023, foundGame.getReleaseYear());
        assertEquals("Action", foundGame.getGenre());
    }

    @Test
    public void testUpdate() throws SQLException {
        Game game = new Game();
        game.setTitle("Original Game");
        game.setDeveloperId(1);
        game.setReleaseYear(2022);
        game.setGenre("Adventure");

        Game createdGame = gameRepository.create(game);
        createdGame.setTitle("Updated Game");
        createdGame.setReleaseYear(2023);
        createdGame.setGenre("RPG");

        boolean updated = gameRepository.update(createdGame);
        assertTrue(updated, "Обновление игры должно быть успешным");

        Game updatedGame = gameRepository.getById(createdGame.getGameId());
        assertNotNull(updatedGame, "Игра должна быть найдена после обновления");
        assertEquals("Updated Game", updatedGame.getTitle());
        assertEquals(2023, updatedGame.getReleaseYear());
        assertEquals("RPG", updatedGame.getGenre());
    }

    @Test
    public void testDelete() throws SQLException {
        Game game = new Game();
        game.setTitle("Game To Delete");
        game.setDeveloperId(1);
        game.setReleaseYear(2020);
        game.setGenre("Strategy");

        Game createdGame = gameRepository.create(game);
        boolean deleted = gameRepository.delete(createdGame.getGameId());
        assertTrue(deleted, "Удаление игры должно быть успешным");

        Game foundGame = gameRepository.getById(createdGame.getGameId());
        assertNull(foundGame, "Игра не должна быть найдена после удаления");
    }

}