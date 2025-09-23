package alef.gameRatingsAPI.domain.game;

import alef.gameRatingsAPI.domain.esrbRating.EsrbRating;
import alef.gameRatingsAPI.domain.esrbRating.EsrbRatingService;
import alef.gameRatingsAPI.domain.genre.Genre;
import alef.gameRatingsAPI.domain.genre.GenreService;
import alef.gameRatingsAPI.domain.platform.Platform;
import alef.gameRatingsAPI.domain.platform.PlatformService;
import alef.gameRatingsAPI.shared.dto.PageDTO;
import alef.gameRatingsAPI.infra.external.api.ExternalApiClient;
import alef.gameRatingsAPI.infra.external.api.ExternalGamesResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public PageDTO<topGamesDTO> findTopGames(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Game> gamesPage = gameRepository.findTopGames(pageable);
        List<topGamesDTO> topGamesDtoList =
                gamesPage.stream().map(topGamesDTO::new).toList();
        return new PageDTO<topGamesDTO>(gamesPage.getNumber(),
                        gamesPage.getSize(),
                        gamesPage.getTotalElements(),
                        gamesPage.getTotalPages(),
                        gamesPage.isLast(),
                        topGamesDtoList);
    }

    public void resyncAllGames(int maxPages) {
        Mono.fromRunnable(() -> {
            System.out.println("\nDatabase synchronization started.");
            for (String genre : GENRE_SLUGS) {
                resyncGamesFromGenre(genre, maxPages);
            }
            System.out.println("\nDatabase synchronization finished.");
        }).subscribeOn(Schedulers.boundedElastic()).subscribe();
    }

    private void resyncGamesFromGenre(String genre, int maxPages) {
        int currentPage = 1;
        System.out.println("\nSyncing genre: " + genre);
        while (currentPage <= maxPages) {
            try {
                ExternalGamesResponseDTO pageInfo =
                        resyncGamesFromPage(genre, 40, currentPage,
                                "-metacritic");
                if (Objects.isNull(pageInfo) || Objects.isNull(pageInfo.next())) {
                    break;
                }
                currentPage++;
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("---- Sync interrupted. ----");
                break;
            } catch (Exception e) {
                System.out.println("---- Error during sync: " + e.getMessage() + " ----");
                break;
            }
        }
    }

    private ExternalGamesResponseDTO resyncGamesFromPage(String genre,
                                                         Integer pageSize,
                                                         Integer pageNumber,
                                                         String ordering) {
        System.out.println("\nSyncing page: " + pageNumber);
        ExternalGamesResponseDTO response =
                apiClient.fetchGamesPage(Set.of(genre), pageSize,
                        pageNumber, ordering).block();
        if (Objects.nonNull(response) && Objects.nonNull(response.results())) {
            insertAndUpdateGames(response.results());
        }
        return response;
    }

    @Transactional
    private void insertAndUpdateGames(List<GameDTO> games) {
        for (GameDTO gameInfo : games) {
            System.out.println(gameInfo.id() + " - " + gameInfo.name());
            EsrbRating esrbRating =
                    esrbRatingService.findOrCreateEsrbRating(gameInfo.esrb_rating());
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
