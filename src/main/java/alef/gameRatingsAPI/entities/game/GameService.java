package alef.gameRatingsAPI.entities.game;

import alef.gameRatingsAPI.entities.esrbRating.EsrbRating;
import alef.gameRatingsAPI.entities.esrbRating.EsrbRatingService;
import alef.gameRatingsAPI.entities.genre.Genre;
import alef.gameRatingsAPI.entities.genre.GenreService;
import alef.gameRatingsAPI.entities.platform.Platform;
import alef.gameRatingsAPI.entities.platform.PlatformService;
import alef.gameRatingsAPI.shared.external.ExternalApiClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class GameService {
    private final ExternalApiClient apiClient;
    private final GameRepository gameRepository;
    private final EsrbRatingService esrbRatingService;
    private final PlatformService platformService;
    private final GenreService genreService;
    private static final List<String> GENRE_SLUGS = List.of(
            "action",
            "indie",
            "adventure",
            "role-playing-games-rpg",
            "strategy",
            "shooter",
            "casual",
            "simulation",
            "puzzle",
            "arcade",
            "platformer",
            "massively-multiplayer",
            "racing",
            "sports",
            "fighting",
            "family",
            "board-games",
            "card",
            "educational"
    );


    public GameService(ExternalApiClient apiClient,
                       GameRepository gameRepository,
                       EsrbRatingService esrbRatingService,
                       PlatformService platformService,
                       GenreService genreService) {
        this.apiClient = apiClient;
        this.gameRepository = gameRepository;
        this.esrbRatingService = esrbRatingService;
        this.platformService = platformService;
        this.genreService = genreService;
    }

    public void fetchAndResyncAllGames(int maxPages) {
        Mono.fromRunnable(() -> {
            int currentPage;
            for (String genre : GENRE_SLUGS) {
                currentPage = 1;
                System.out.println("\nFetching from genre: " + genre);
                while (currentPage <= maxPages) {
                    try {
                        System.out.println("\nCurrent page: " + currentPage);
                        ExternalGamesResponseDTO response =
                                apiClient.fetchGamesPage(Set.of(genre), 40,
                                        currentPage, "-metacritic").block();
                        if (Objects.nonNull(response) && Objects.nonNull(response.results())) {
                            saveAndUpdateGames(response.results());
                        }
                        if (Objects.isNull(response) || Objects.isNull(response.next())) {
                            break;
                        }
                        currentPage++;
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Sync interrupted.");
                        break;
                    } catch (Exception e) {
                        System.out.println("Error during sync: " + e.getMessage());
                        break;
                    }
                }
            }
            System.out.println("\nDatabase synchronization finished.");
        }).subscribeOn(Schedulers.boundedElastic()).subscribe();
    }


    @Transactional
    private void saveAndUpdateGames(List<GameDTO> gamesFetchedFromExternalApi) {
        for (GameDTO gameInfo : gamesFetchedFromExternalApi) {
            System.out.println(gameInfo.id() + " - " + gameInfo.name());
            //System.out.println(gameInfo.platforms());
            EsrbRating esrbRating =
                    esrbRatingService.getEsrbRating(gameInfo.esrb_rating());
            Set<Platform> platforms =
                    platformService.getPlatformsList(gameInfo.platforms());
            Set<Genre> genres = genreService.getGenresList(gameInfo.genres());

            Game game =
                    gameRepository.findById(gameInfo.id()).orElseGet(Game::new);
            game.setId(gameInfo.id());
            game.setSlug(gameInfo.slug());
            game.setName(gameInfo.name());
            game.setTba(gameInfo.tba());
            game.setReleaseDate(gameInfo.released());
            game.setBackgroundImage(gameInfo.background_image());
            game.setRating(gameInfo.rating());
            game.setTopRating(gameInfo.rating_top());
            game.setMetacritic(gameInfo.metacritic());
            game.setRefreshTime(gameInfo.updated());
            game.setEsrbRating(esrbRating);
            game.setPlatforms(platforms);
            game.setGenres(genres);

            gameRepository.save(game);
        }
    }


    public void testSingleFetch() {
        System.out.println(
                "----------------------------------------------------");
        apiClient.fetchGamesPage(null, null, null, null).subscribe(response -> {
            System.out.println("Response received");
            System.out.println("Count: " + response.count());
            System.out.println("Next: " + response.next());
            System.out.println("Previous: " + response.previous());
            System.out.println("Total Results: " + response.results().size());
            System.out.println("Results: " + response.results());
            long gamesWithSlug =
                    response.results().stream().filter(game -> Objects.nonNull(game.slug())).count();
            System.out.println("Games with slug: " + gamesWithSlug);
        }, error -> System.err.println("Request error: " + error.getMessage()));
        System.out.println("Continue");
    }
}
