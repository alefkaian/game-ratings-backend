package alef.gameRatingsAPI.entities.game;

import java.util.List;

public record ExternalGamesResponseDTO(
        int count,
        String next,
        String previous,
        List<GameDTO> results
) {
}
