package org.leon.astonhome.service;

import org.leon.astonhome.dto.GameDTO;
import org.leon.astonhome.entity.Game;
import org.leon.astonhome.mapper.GameMapper;
import org.leon.astonhome.repository.GameRepository;

import java.util.List;
import java.util.stream.Collectors;

public class GameService {
    private final GameRepository repository;
    private final GameMapper gameMapper;

    public GameService(GameRepository repository, GameMapper gameMapper) {
        this.repository = repository;
        this.gameMapper = gameMapper;
    }

    public GameDTO createGame(GameDTO dto) {
        Game game = gameMapper.toEntity(dto);
        Game created = repository.create(game);
        return gameMapper.toDto(created);
    }

    public GameDTO getGameById(int id) {
        Game game = repository.getById(id);
        return gameMapper.toDto(game);
    }

    public List<GameDTO> getAllGames() {
        List<Game> games = repository.getAll();
        return games.stream().map(gameMapper::toDto).collect(Collectors.toList());
    }

    public boolean updateGame(int id, GameDTO dto) {
        Game game = gameMapper.toEntity(dto);
        game.setGameId(id);
        return repository.update(game);
    }

    public boolean deleteGame(int id) {
        return repository.delete(id);
    }
}
