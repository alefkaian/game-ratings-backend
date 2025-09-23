package alef.gameRatingsAPI.domain.game;

import alef.gameRatingsAPI.domain.esrbRating.EsrbRatingDTO;
import alef.gameRatingsAPI.domain.genre.GenreDTO;
import alef.gameRatingsAPI.domain.platform.PlatformWrapperDTO;

import java.time.LocalDate;
import java.util.List;

public record GameDTO(
        long id,
        String slug,
        String name,
        boolean tba,
        LocalDate released,
        String background_image,
        double rating,
        double rating_top,
        int metacritic,
        LocalDate updated,
        List<PlatformWrapperDTO> platforms,
        List<GenreDTO> genres,
        EsrbRatingDTO esrb_rating
) {
}
