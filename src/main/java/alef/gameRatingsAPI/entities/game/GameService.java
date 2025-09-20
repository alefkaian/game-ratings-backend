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

    public GameService(ExternalApiClient apiClient, GameRepository gameRepository, EsrbRatingService esrbRatingService, PlatformService platformService, GenreService genreService) {
        this.apiClient = apiClient;
        this.gameRepository = gameRepository;
        this.esrbRatingService = esrbRatingService;
        this.platformService = platformService;
        this.genreService = genreService;
    }

    /*public Mono<Void> fetchAndRefreshAllGames(String initialUrl) {
        return apiClient.fetchGamesPage(initialUrl, 40)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(response -> {
                    saveAndUpdateGames(response.results());

                    // só chama recursivamente se houver next
                    if (response.next() != null) {
                        return fetchAndRefreshAllGames(response.next());
                    } else {
                        return Mono.empty();
                    }
                });
    }*/
    public void fetchAndRefreshAllGames(String initialUrl) {
        Mono.fromRunnable(() -> {
            String currentUrl = initialUrl;
            int counter = 0;
            while (true) {
                try {
                    System.out.println("\nCounter: " + counter);
                    counter++;
                    // A chamada reativa da API é bloqueada aqui,
                    // garantindo que a resposta chegue antes de prosseguir.
                    ExternalGamesResponseDTO response = apiClient.fetchGamesPage(currentUrl, 40).block();

                    // Valida a resposta antes de processar
                    if (Objects.nonNull(response) && Objects.nonNull(response.results())) {
                        saveAndUpdateGames(response.results());
                    }

                    // A iteração só para quando a URL da próxima página for nula
                    if (Objects.isNull(response) || Objects.isNull(response.next())) {
                        break;
                    }

                    currentUrl = response.next();

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Sync interrupted.");
                    break;
                } catch (Exception e) {
                    System.out.println("Error during sync: " + e.getMessage());
                    break;
                }
            }
        }).subscribeOn(Schedulers.boundedElastic()).subscribe(); // Executa a lógica em uma thread dedicada
    }


    @Transactional
    private void saveAndUpdateGames(List<GameDTO> gamesFetchedFromExternalApi) {
        for (GameDTO gameInfo : gamesFetchedFromExternalApi) {
            System.out.println(gameInfo.id() + " - " + gameInfo.name());
            System.out.println(gameInfo.platforms());
            EsrbRating esrbRating = esrbRatingService.getEsrbRating(gameInfo.esrb_rating());
            Set<Platform> platforms = platformService.getPlatformsList(gameInfo.platforms());
            Set<Genre> genres = genreService.getGenresList(gameInfo.genres());

            Game game = gameRepository.findById(gameInfo.id()).orElseGet(Game::new);
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


    public void testSingleFetch(String initialUrl) {
        System.out.println("----------------------------------------------------");
        apiClient.fetchGamesPage(initialUrl, 40)
                .subscribe(response -> {
                            System.out.println("Response received");
                            System.out.println("Count: " + response.count());
                            System.out.println("Next: " + response.next());
                            System.out.println("Previous: " + response.previous());
                            System.out.println("Total Results: " + response.results().size());
                            System.out.println("Results: " + response.results());
                            long gamesWithSlug = response.results().stream().filter(game -> Objects.nonNull(game.slug())).count();
                            System.out.println("Games with slug: " + gamesWithSlug);
                        },
                        error -> {
                            System.err.println("Request error: " + error.getMessage());
                        });
        System.out.println("Continue");
    }
}
