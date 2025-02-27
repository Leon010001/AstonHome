package org.leon.astonhome.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leon.astonhome.dto.PlayerDTO;
import org.leon.astonhome.service.PlayerService;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.lang.reflect.Field;
import static org.mockito.Mockito.*;

class PlayerServletTest {
    private PlayerServlet servlet;
    private PlayerService serviceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new PlayerServlet();
        serviceMock = mock(PlayerService.class);
        Field serviceField = PlayerServlet.class.getDeclaredField("playerService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, serviceMock);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");

        PlayerDTO dto = new PlayerDTO();
        dto.setPlayerId(1);
        dto.setName("Player");
        dto.setEmail("player@example.com");
        dto.setRegistrationDate("2022-02-03");

        when(serviceMock.getPlayerById(1)).thenReturn(dto);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"playerId\":1"));
        assertTrue(output.contains("\"email\":\"player@example.com\""));
    }

    @Test
    void testDoPost() throws ServletException, IOException {
        String jsonInput = "{\"name\":\"Player\", \"email\":\"player@example.com\", \"registrationDate\":\"2022-02-03\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        PlayerDTO createdDto = new PlayerDTO();
        createdDto.setPlayerId(2);
        createdDto.setName("Player");
        createdDto.setEmail("player@example.com");
        createdDto.setRegistrationDate("2022-02-03");

        when(serviceMock.createPlayer(any(PlayerDTO.class))).thenReturn(createdDto);

        servlet.doPost(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"playerId\":2"));
    }

    @Test
    void testDoPut() throws ServletException, IOException {
        String jsonInput = "{\"playerId\":1,\"name\":\"New Player Updated\", \"email\":\"newPlayer@example.com\", \"registrationDate\":\"2022-02-03\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(serviceMock.updatePlayer(eq(1), any(PlayerDTO.class))).thenReturn(true);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"playerId\":1"));
        assertTrue(output.contains("New Player Updated"));
    }

    @Test
    void testDoDelete() throws ServletException, IOException {

        when(request.getPathInfo()).thenReturn("/1");
        when(serviceMock.deletePlayer(1)).thenReturn(true);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);

        String output = responseWriter.toString();
        assertTrue(output.contains("Player deleted"));
    }
}