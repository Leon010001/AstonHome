package org.leon.astonhome.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leon.astonhome.dto.DeveloperDTO;
import org.leon.astonhome.service.DeveloperService;

import java.io.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeveloperServletTest {
    private DeveloperServlet servlet;
    private DeveloperService serviceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new DeveloperServlet();
        serviceMock = mock(DeveloperService.class);
        Field serviceField = DeveloperServlet.class.getDeclaredField("developerService");
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

        DeveloperDTO dto = new DeveloperDTO();
        dto.setDeveloperId(1);
        dto.setName("Test Developer");
        dto.setFoundationYear(2000);

        when(serviceMock.getDeveloperById(1)).thenReturn(dto);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"developerId\":1"));
        assertTrue(output.contains("\"name\":\"Test Developer\""));
        assertTrue(output.contains("\"foundationYear\":2000"));
    }

    @Test
    void testDoPost() throws ServletException, IOException {
        String jsonInput = "{\"name\":\"New Developer\", \"foundationYear\":2021}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        DeveloperDTO createdDto = new DeveloperDTO();
        createdDto.setDeveloperId(2);
        createdDto.setName("New Developer");
        createdDto.setFoundationYear(2021);

        when(serviceMock.createDeveloper(any(DeveloperDTO.class))).thenReturn(createdDto);

        servlet.doPost(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"developerId\":2"));
    }

    @Test
    void testDoPut() throws ServletException, IOException {
        String jsonInput = "{\"developerId\":1,\"name\":\"New Developer Updated\", \"foundationYear\":2020}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(serviceMock.updateDeveloper(eq(1), any(DeveloperDTO.class))).thenReturn(true);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"developerId\":1"));
        assertTrue(output.contains("New Developer Updated"));
    }

    @Test
    void testDoDelete() throws ServletException, IOException {

        when(request.getPathInfo()).thenReturn("/1");
        when(serviceMock.deleteDeveloper(1)).thenReturn(true);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);

        String output = responseWriter.toString();
        assertTrue(output.contains("Developer deleted"));
    }
}