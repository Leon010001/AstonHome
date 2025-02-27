package org.leon.astonhome.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.leon.astonhome.mapper.DeveloperMapper;
import org.leon.astonhome.repository.DeveloperRepository;
import org.leon.astonhome.service.DeveloperService;
import org.leon.astonhome.dto.DeveloperDTO;
import org.leon.astonhome.util.JsonUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/developers/*")
public class DeveloperServlet extends HttpServlet {
    private final DeveloperService developerService = new DeveloperService(new DeveloperRepository(), new DeveloperMapper());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<DeveloperDTO> developers = developerService.getAllDevelopers();
            JsonUtil.sendJsonResponse(resp, developers, HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                DeveloperDTO developer = developerService.getDeveloperById(id);
                JsonUtil.checkExists(developer, "Developer not found", HttpServletResponse.SC_NOT_FOUND);
                JsonUtil.sendJsonResponse(resp, developer, HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
            } catch (JsonUtil.NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DeveloperDTO dto = JsonUtil.parseJsonRequest(req, DeveloperDTO.class);
        try {
            JsonUtil.checkDto(dto);
            DeveloperDTO created = developerService.createDeveloper(dto);
            JsonUtil.sendJsonResponse(resp, created, HttpServletResponse.SC_CREATED);
        } catch (JsonUtil.InvalidDtoException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DeveloperDTO dto = JsonUtil.parseJsonRequest(req, DeveloperDTO.class);
        try {
            JsonUtil.checkDto(dto);
            boolean updated = developerService.updateDeveloper(dto.getDeveloperId(), dto);
            if (updated) {
                JsonUtil.sendJsonResponse(resp, dto, HttpServletResponse.SC_OK);
            } else {
                throw new JsonUtil.NotFoundException("Developer not found");
            }
        } catch (JsonUtil.InvalidDtoException | JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendErrorResponse(resp, "Missing developer ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = developerService.deleteDeveloper(id);
            JsonUtil.checkDeletion(deleted, "Developer not found", HttpServletResponse.SC_NOT_FOUND);
            JsonUtil.sendJsonResponse(resp, "Developer deleted", HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
        } catch (JsonUtil.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
