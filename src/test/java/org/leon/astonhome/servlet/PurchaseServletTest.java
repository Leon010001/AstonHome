package org.leon.astonhome.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leon.astonhome.dto.PurchaseDTO;
import org.leon.astonhome.service.PurchaseService;

import java.io.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseServletTest {

    private PurchaseServlet servlet;
    private PurchaseService purchaseServiceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new PurchaseServlet();
        purchaseServiceMock = mock(PurchaseService.class);

        Field serviceField = PurchaseServlet.class.getDeclaredField("purchaseService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, purchaseServiceMock);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");

        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setPurchaseId(1);
        purchaseDTO.setGameId(1);
        purchaseDTO.setPlayerId(1);
        purchaseDTO.setPurchaseDate("2023-10-15");

        when(purchaseServiceMock.getPurchaseById(1)).thenReturn(purchaseDTO);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"purchaseId\":1"));
        assertTrue(output.contains("\"purchaseDate\":\"2023-10-15\""));
    }

    @Test
    void testDoPost() throws ServletException, IOException {
        String jsonInput = "{\"gameId\":1, \"playerId\":1, \"purchaseDate\":\"2023-10-15\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        PurchaseDTO createdDto = new PurchaseDTO();
        createdDto.setPurchaseId(2);
        createdDto.setGameId(1);
        createdDto.setPlayerId(1);
        createdDto.setPurchaseDate("2023-10-15");

        when(purchaseServiceMock.createPurchase(any(PurchaseDTO.class))).thenReturn(createdDto);

        servlet.doPost(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"purchaseId\":2"));
        assertTrue(output.contains("\"purchaseDate\":\"2023-10-15\""));
    }

    @Test
    void testDoPut() throws ServletException, IOException {
        String jsonInput = "{\"purchaseId\":1, \"gameId\":2, \"playerId\":1, \"purchaseDate\":\"2023-10-20\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(purchaseServiceMock.updatePurchase(eq(1), any(PurchaseDTO.class))).thenReturn(true);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"purchaseId\":1"));
        assertTrue(output.contains("\"purchaseDate\":\"2023-10-20\""));
    }

    @Test
    void testDoDelete() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(purchaseServiceMock.deletePurchase(1)).thenReturn(true);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("Purchase deleted"));
    }

}