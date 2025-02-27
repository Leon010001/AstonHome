package org.leon.astonhome.service;

import org.leon.astonhome.dto.PlayerDTO;
import org.leon.astonhome.entity.Player;
import org.leon.astonhome.mapper.PlayerMapper;
import org.leon.astonhome.repository.PlayerRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerService {
    private final PlayerRepository repository;
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository repository, PlayerMapper playerMapper) {
        this.repository = repository;
        this.playerMapper = playerMapper;
    }

    public PlayerDTO createPlayer(PlayerDTO dto) {
        Player player = playerMapper.toEntity(dto);
        Player created = repository.create(player);
        return playerMapper.toDto(created);
    }

    public PlayerDTO getPlayerById(int id) {
        Player player = repository.getById(id);
        return playerMapper.toDto(player);
    }

    public List<PlayerDTO> getAllPlayers() {
        List<Player> players = repository.getAll();
        return players.stream().map(playerMapper::toDto).collect(Collectors.toList());
    }

    public boolean updatePlayer(int id, PlayerDTO dto) {
        Player player = playerMapper.toEntity(dto);
        player.setPlayerId(id);
        return repository.update(player);
    }

    public boolean deletePlayer(int id) {
        return repository.delete(id);
    }
}
