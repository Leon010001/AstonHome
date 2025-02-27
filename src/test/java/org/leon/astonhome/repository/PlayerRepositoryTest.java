package org.leon.astonhome.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.*;
import org.leon.astonhome.connect.DBConnection;
import org.leon.astonhome.entity.Player;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerRepositoryTest {
    private PlayerRepository playerRepository;

    @BeforeAll
    public void setUp() {
        playerRepository = new PlayerRepository();
    }


    @BeforeEach
    public void clearData() {
        DBConnection.executePreparedStatement("TRUNCATE TABLE players RESTART IDENTITY CASCADE",
                stmt -> {},
                stmt -> stmt.executeUpdate());
    }

    @Test
    public void testCreate() {
        Player player = new Player();
        player.setName("Test Player");
        player.setEmail("testqq@example.com");

        Player createdPlayer = playerRepository.create(player);
        Assertions.assertNotNull(createdPlayer, "Созданный игрок не должен быть null");
        Assertions.assertNotNull(createdPlayer.getPlayerId(), "У игрока должен быть присвоен ID после создания");
    }

    @Test
    public void testUpdate() {
        Player player = new Player();
        player.setName("Original Player");
        player.setEmail("originalPlayer@example.com");

        Player created = playerRepository.create(player);
        created.setName("Updated Player");
        created.setEmail("updatedPlayer@example.com");

        boolean updated = playerRepository.update(created);
        assertTrue(updated);

        Player updatedPlayer = playerRepository.getById(created.getPlayerId());
        assertNotNull(updatedPlayer);
        assertEquals("Updated Player", updatedPlayer.getName());
        assertEquals("updatedPlayer@example.com", updatedPlayer.getEmail());
    }

    @Test
    public void testDelete() {
        Player player = new Player();
        player.setName("Delete Player");
        player.setEmail("deletedPlayer@example.com)");
        Player created = playerRepository.create(player);

        boolean deleted = playerRepository.delete(created.getPlayerId());
        assertTrue(deleted);

        Player found = playerRepository.getById(created.getPlayerId());
        assertNull(found);
    }
}