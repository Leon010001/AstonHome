package org.leon.astonhome.mapper;

import org.leon.astonhome.dto.GameDTO;
import org.leon.astonhome.entity.Game;

public class GameMapper implements Mapper<Game, GameDTO > {
    @Override
    public GameDTO toDto(Game game) {
        if (game == null) return null;
        GameDTO dto = new GameDTO();
        dto.setGameId(game.getGameId());
        dto.setTitle(game.getTitle());
        dto.setDeveloperId(game.getDeveloperId());
        dto.setReleaseYear(game.getReleaseYear());
        dto.setGenre(game.getGenre());
        return dto;
    }

    @Override
    public Game toEntity(GameDTO dto) {
        if (dto == null) return null;
        Game game = new Game();
        game.setGameId(dto.getGameId());
        game.setTitle(dto.getTitle());
        game.setDeveloperId(dto.getDeveloperId());
        game.setReleaseYear(dto.getReleaseYear());
        game.setGenre(dto.getGenre());
        return game;
    }
}
