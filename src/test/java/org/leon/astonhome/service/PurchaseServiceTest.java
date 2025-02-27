package org.leon.astonhome.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leon.astonhome.dto.PurchaseDTO;
import org.leon.astonhome.entity.Purchase;
import org.leon.astonhome.mapper.PurchaseMapper;
import org.leon.astonhome.repository.PurchaseRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PurchaseServiceTest {

    private PurchaseRepository purchaseRepositoryMock;
    private PurchaseMapper purchaseMapperMock;
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        purchaseRepositoryMock = mock(PurchaseRepository.class);
        purchaseMapperMock = mock(PurchaseMapper.class);
        purchaseService = new PurchaseService(purchaseRepositoryMock, purchaseMapperMock);
    }

    @Test
    void testCreatePurchase() {

        PurchaseDTO inputDto = new PurchaseDTO();
        inputDto.setGameId(1);
        inputDto.setPlayerId(1);
        inputDto.setPurchaseDate("2023-10-15");

        Purchase purchaseEntity = new Purchase();
        purchaseEntity.setGameId(1);
        purchaseEntity.setPlayerId(1);
        purchaseEntity.setPurchaseDate("2023-10-15");

        Purchase createdPurchase = new Purchase();
        createdPurchase.setPurchaseId(1);
        createdPurchase.setGameId(1);
        createdPurchase.setPlayerId(1);
        createdPurchase.setPurchaseDate("2023-10-15");

        PurchaseDTO outputDto = new PurchaseDTO();
        outputDto.setPurchaseId(1);
        outputDto.setGameId(1);
        outputDto.setPlayerId(1);
        outputDto.setPurchaseDate("2023-10-15");

        when(purchaseMapperMock.toEntity(inputDto)).thenReturn(purchaseEntity);
        when(purchaseRepositoryMock.create(purchaseEntity)).thenReturn(createdPurchase);
        when(purchaseMapperMock.toDto(createdPurchase)).thenReturn(outputDto);

        PurchaseDTO result = purchaseService.createPurchase(inputDto);
        assertNotNull(result);
        assertEquals(1, result.getPurchaseId());
        assertEquals("2023-10-15", result.getPurchaseDate());

        verify(purchaseMapperMock).toEntity(inputDto);
        verify(purchaseRepositoryMock).create(any(Purchase.class));
        verify(purchaseMapperMock).toDto(any(Purchase.class));
    }

    @Test
    void testGetPurchaseById() {
        int id = 1;

        Purchase purchaseEntity = new Purchase();
        purchaseEntity.setPurchaseId(id);
        purchaseEntity.setGameId(1);
        purchaseEntity.setPlayerId(1);
        purchaseEntity.setPurchaseDate("2023-10-15");

        PurchaseDTO outputDto = new PurchaseDTO();
        outputDto.setPurchaseId(id);
        outputDto.setGameId(1);
        outputDto.setPlayerId(1);
        outputDto.setPurchaseDate("2023-10-15");

        when(purchaseRepositoryMock.getById(id)).thenReturn(purchaseEntity);
        when(purchaseMapperMock.toDto(purchaseEntity)).thenReturn(outputDto);

        PurchaseDTO result = purchaseService.getPurchaseById(id);
        assertNotNull(result);
        assertEquals(id, result.getPurchaseId());
        assertEquals("2023-10-15", result.getPurchaseDate());
        verify(purchaseRepositoryMock).getById(id);
        verify(purchaseMapperMock).toDto(any(Purchase.class));
    }

    @Test
    void testUpdatePurchase() {
        int id = 1;
        PurchaseDTO inputDto = new PurchaseDTO();
        inputDto.setPurchaseId(id);
        inputDto.setGameId(2);
        inputDto.setPlayerId(1);
        inputDto.setPurchaseDate("2023-10-20");

        Purchase purchaseEntity = new Purchase();
        purchaseEntity.setPurchaseId(id);
        purchaseEntity.setGameId(2);
        purchaseEntity.setPlayerId(1);
        purchaseEntity.setPurchaseDate("2023-10-20");

        when(purchaseMapperMock.toEntity(inputDto)).thenReturn(purchaseEntity);
        when(purchaseRepositoryMock.update(purchaseEntity)).thenReturn(true);

        boolean result = purchaseService.updatePurchase(id, inputDto);
        assertTrue(result);
        verify(purchaseMapperMock).toEntity(inputDto);
        verify(purchaseRepositoryMock).update(argThat(entity ->
                entity.getPurchaseId() == id &&
                        entity.getGameId() == 2 &&
                        entity.getPurchaseDate().equals("2023-10-20")));
    }

    @Test
    void testDeletePurchase() {
        int id = 1;
        when(purchaseRepositoryMock.delete(id)).thenReturn(true);
        boolean result = purchaseService.deletePurchase(id);
        assertTrue(result);
        verify(purchaseRepositoryMock).delete(id);
    }
}