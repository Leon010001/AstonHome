package org.leon.astonhome.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.leon.astonhome.dto.GameDTO;
import org.leon.astonhome.mapper.GameMapper;
import org.leon.astonhome.repository.GameRepository;
import org.leon.astonhome.service.GameService;
import org.leon.astonhome.util.JsonUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/games/*")
public class GameServlet extends HttpServlet {
    private final GameService gameService = new GameService(new GameRepository(), new GameMapper());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<GameDTO> games = gameService.getAllGames();
            JsonUtil.sendJsonResponse(resp, games, HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                GameDTO game = gameService.getGameById(id);
                JsonUtil.checkExists(game, "Game not found", HttpServletResponse.SC_NOT_FOUND);
                JsonUtil.sendJsonResponse(resp, game, HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
            } catch (JsonUtil.NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GameDTO gameDto = JsonUtil.parseJsonRequest(req, GameDTO.class);
        try {
            JsonUtil.checkDto(gameDto);
            GameDTO createdGame = gameService.createGame(gameDto);
            JsonUtil.sendJsonResponse(resp, createdGame, HttpServletResponse.SC_CREATED);
        } catch (JsonUtil.InvalidDtoException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GameDTO gameDto = JsonUtil.parseJsonRequest(req, GameDTO.class);
        try {
            JsonUtil.checkDto(gameDto);
            boolean updated = gameService.updateGame(gameDto.getGameId(), gameDto);
            if (updated) {
                JsonUtil.sendJsonResponse(resp, gameDto, HttpServletResponse.SC_OK);
            } else {
                throw new JsonUtil.NotFoundException("Game not found");
            }
        } catch (JsonUtil.InvalidDtoException | JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendErrorResponse(resp, "Missing game ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = gameService.deleteGame(id);
            JsonUtil.checkDeletion(deleted, "Game not found", HttpServletResponse.SC_NOT_FOUND);
            JsonUtil.sendJsonResponse(resp, "Game deleted", HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
        } catch (JsonUtil.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
