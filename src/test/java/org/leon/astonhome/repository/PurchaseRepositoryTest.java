package org.leon.astonhome.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.leon.astonhome.connect.DBConnection;
import org.leon.astonhome.entity.Purchase;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PurchaseRepositoryTest {

    private PurchaseRepository purchaseRepository;

    @BeforeAll
    public void setUp() {
        purchaseRepository = new PurchaseRepository();
    }

    @BeforeEach
    public void clearData() throws SQLException {
        DBConnection.executePreparedStatement("TRUNCATE TABLE purchases RESTART IDENTITY CASCADE",
                stmt -> {},
                stmt -> stmt.executeUpdate());
    }

    @Test
    public void testCreate() throws SQLException {

        Purchase purchase = new Purchase();
        purchase.setGameId(1);
        purchase.setPlayerId(1);
        purchase.setPurchaseDate("2023-10-15");

        Purchase createdPurchase = purchaseRepository.create(purchase);
        assertNotNull(createdPurchase, "Созданная покупка не должна быть null");
        assertNotNull(createdPurchase.getPurchaseId(), "У покупки должен быть присвоен ID после создания");

    }

    @Test
    public void testUpdate() throws SQLException {

        Purchase purchase = new Purchase();
        purchase.setGameId(1);
        purchase.setPlayerId(2);
        purchase.setPurchaseDate("2023-10-15");

        Purchase createdPurchase = purchaseRepository.create(purchase);
        createdPurchase.setGameId(2);
        createdPurchase.setPlayerId(2);
        createdPurchase.setPurchaseDate("2023-10-20");

        boolean updated = purchaseRepository.update(createdPurchase);
        assertTrue(updated, "Обновление покупки должно быть успешным");

        Purchase updatedPurchase = purchaseRepository.getById(createdPurchase.getPurchaseId());
        assertNotNull(updatedPurchase, "Покупка должна быть найдена после обновления");
        assertEquals(2, updatedPurchase.getGameId());
        assertEquals(2, updatedPurchase.getPlayerId());
        assertEquals("2023-10-20", updatedPurchase.getPurchaseDate());
    }

    @Test
    public void testDelete() throws SQLException {

        Purchase purchase = new Purchase();
        purchase.setGameId(1);
        purchase.setPlayerId(1);
        purchase.setPurchaseDate("2023-10-15");

        Purchase createdPurchase = purchaseRepository.create(purchase);
        boolean deleted = purchaseRepository.delete(createdPurchase.getPurchaseId());
        assertTrue(deleted, "Удаление покупки должно быть успешным");

        Purchase foundPurchase = purchaseRepository.getById(createdPurchase.getPurchaseId());
        assertNull(foundPurchase, "Покупка не должна быть найдена после удаления");
    }

}