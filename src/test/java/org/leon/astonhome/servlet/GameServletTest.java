package org.leon.astonhome.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leon.astonhome.dto.DeveloperDTO;
import org.leon.astonhome.dto.GameDTO;
import org.leon.astonhome.service.GameService;

import java.io.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServletTest {

    private GameServlet servlet;
    private GameService gameServiceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new GameServlet();
        gameServiceMock = mock(GameService.class);
        Field serviceField = GameServlet.class.getDeclaredField("gameService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, gameServiceMock);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");

        GameDTO gameDTO = new GameDTO();
        gameDTO.setGameId(1);
        gameDTO.setTitle("Test Game");
        gameDTO.setDeveloperId(1);
        gameDTO.setReleaseYear(2023);
        gameDTO.setGenre("Action");

        when(gameServiceMock.getGameById(1)).thenReturn(gameDTO);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"gameId\":1"));
        assertTrue(output.contains("\"title\":\"Test Game\""));
    }

    @Test
    void testDoPost() throws ServletException, IOException {
        String jsonInput = "{\"title\":\"New Game\", \"developerId\":1, \"releaseYear\":2023, \"genre\":\"RPG\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        GameDTO createdDto = new GameDTO();
        createdDto.setGameId(2);
        createdDto.setTitle("New Game");
        createdDto.setDeveloperId(1);
        createdDto.setReleaseYear(2023);
        createdDto.setGenre("RPG");

        when(gameServiceMock.createGame(any(GameDTO.class))).thenReturn(createdDto);

        servlet.doPost(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"gameId\":2"));
    }

    @Test
    void testDoPut() throws ServletException, IOException {
        String jsonInput = "{\"gameId\":1,\"title\":\"Updated Game\", \"developerId\":1, \"releaseYear\":2023, \"genre\":\"RPG\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(gameServiceMock.updateGame(eq(1), any(GameDTO.class))).thenReturn(true);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"gameId\":1"));
        assertTrue(output.contains("Updated Game"));
    }

    @Test
    void testDoDelete() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(gameServiceMock.deleteGame(1)).thenReturn(true);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("Game deleted"));
    }
}