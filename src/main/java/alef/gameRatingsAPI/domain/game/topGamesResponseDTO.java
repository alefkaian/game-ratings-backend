package alef.gameRatingsAPI.domain.game;

import alef.gameRatingsAPI.domain.genre.GenreNameDTO;

import java.time.Year;
import java.util.Set;
import java.util.stream.Collectors;

public record topGamesResponseDTO(String slug, String name, double rating,
                                  int metacritic, Set<GenreNameDTO> genres,
                                  Year releaseYear,
                                  String backgroundImage) {
    public topGamesResponseDTO(Game game) {
        this(game.getSlug(), game.getName(), game.getRating(),
                game.getMetacritic(),
                game.getGenres().stream().map(GenreNameDTO::new).collect(Collectors.toSet()),
                Year.of(game.getReleaseDate().getYear()),
                game.getBackgroundImage());
    }
}
