package org.leon.astonhome.mapper;

import org.leon.astonhome.dto.PlayerDTO;
import org.leon.astonhome.entity.Player;

public class PlayerMapper implements Mapper<Player, PlayerDTO> {
    @Override
    public PlayerDTO toDto(Player player) {
        if (player == null) return null;
        PlayerDTO dto = new PlayerDTO();
        dto.setPlayerId(player.getPlayerId());
        dto.setName(player.getName());
        dto.setEmail(player.getEmail());
        dto.setRegistrationDate(player.getRegistrationDate());
        return dto;
    }

    @Override
    public Player toEntity(PlayerDTO dto) {
        if (dto == null) return null;
        Player player = new Player();
        player.setPlayerId(dto.getPlayerId());
        player.setName(dto.getName());
        player.setEmail(dto.getEmail());
        player.setRegistrationDate(dto.getRegistrationDate());
        return player;
    }
}
