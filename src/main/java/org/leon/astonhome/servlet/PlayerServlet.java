package org.leon.astonhome.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.leon.astonhome.dto.PlayerDTO;
import org.leon.astonhome.mapper.PlayerMapper;
import org.leon.astonhome.repository.PlayerRepository;
import org.leon.astonhome.service.PlayerService;
import org.leon.astonhome.util.JsonUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/players/*")
public class PlayerServlet extends HttpServlet {
    private final PlayerService playerService = new PlayerService(new PlayerRepository(), new PlayerMapper());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<PlayerDTO> players = playerService.getAllPlayers();
            JsonUtil.sendJsonResponse(resp, players, HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                PlayerDTO player = playerService.getPlayerById(id);
                JsonUtil.checkExists(player, "Player not found", HttpServletResponse.SC_NOT_FOUND);
                JsonUtil.sendJsonResponse(resp, player, HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
            } catch (JsonUtil.NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PlayerDTO playerDto = JsonUtil.parseJsonRequest(req, PlayerDTO.class);
        try {
            JsonUtil.checkDto(playerDto);
            PlayerDTO createdPlayer = playerService.createPlayer(playerDto);
            JsonUtil.sendJsonResponse(resp, createdPlayer, HttpServletResponse.SC_CREATED);
        } catch (JsonUtil.InvalidDtoException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PlayerDTO playerDto = JsonUtil.parseJsonRequest(req, PlayerDTO.class);
        try {
            JsonUtil.checkDto(playerDto);
            boolean updated = playerService.updatePlayer(playerDto.getPlayerId(), playerDto);
            if (updated) {
                JsonUtil.sendJsonResponse(resp, playerDto, HttpServletResponse.SC_OK);
            } else {
                throw new JsonUtil.NotFoundException("Player not found");
            }
        } catch (JsonUtil.InvalidDtoException | JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendErrorResponse(resp, "Missing player ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = playerService.deletePlayer(id);
            JsonUtil.checkDeletion(deleted, "Player not found", HttpServletResponse.SC_NOT_FOUND);
            JsonUtil.sendJsonResponse(resp, "Player deleted", HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
        } catch (JsonUtil.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
