package org.leon.astonhome.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.leon.astonhome.connect.DBConnection;
import org.leon.astonhome.entity.Developer;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeveloperRepositoryTest {
    private DeveloperRepository repository;

    @BeforeAll
    public void setUp() {
        repository = new DeveloperRepository();
    }

    @BeforeEach
    public void clearData() {
        DBConnection.executePreparedStatement("TRUNCATE TABLE developers RESTART IDENTITY CASCADE",
                stmt -> {},
                stmt -> stmt.executeUpdate());
    }

    @Test
    public void testCreate() {
        Developer developer = new Developer();
        developer.setName("Test Developer");
        developer.setFoundationYear(2000);

        Developer created = repository.create(developer);
        assertNotNull(created);
        assertNotNull(created.getDeveloperId());

    }

    @Test
    public void testUpdate() {
        Developer developer = new Developer();
        developer.setName("Original Name");
        developer.setFoundationYear(1990);
        Developer created = repository.create(developer);

        created.setName("Updated Name");
        created.setFoundationYear(2010);
        boolean updated = repository.update(created);
        assertTrue(updated);

        Developer updatedDev = repository.getById(created.getDeveloperId());
        assertNotNull(updatedDev);
        assertEquals("Updated Name", updatedDev.getName());
        assertEquals(2010, updatedDev.getFoundationYear());
    }

    @Test
    public void testDelete() {
        Developer developer = new Developer();
        developer.setName("Delete Dev");
        developer.setFoundationYear(1970);
        Developer created = repository.create(developer);

        boolean deleted = repository.delete(created.getDeveloperId());
        assertTrue(deleted);

        Developer found = repository.getById(created.getDeveloperId());
        assertNull(found);
    }
}