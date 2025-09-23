package alef.gameRatingsAPI.infra.external.api;

import alef.gameRatingsAPI.domain.game.GameDTO;

import java.util.List;

public record ExternalGamesResponseDTO(
        int count,
        String next,
        String previous,
        List<GameDTO> results
) {
}
