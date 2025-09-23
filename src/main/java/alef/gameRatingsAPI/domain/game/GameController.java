package alef.gameRatingsAPI.domain.game;

import alef.gameRatingsAPI.shared.dto.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
@Tag(name = "games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/top")
    @Operation(summary = "Get a list of top games", description =
            "Returns a" + " " + "list " + "of " + "games ordered by the " +
                    "rating algorithm in descending order")
    public ResponseEntity<PageDTO<topGamesResponseDTO>> listTopGames(
            @Parameter(description = "Page number of the paginated result",
                    example = "0")
            @RequestParam(value = "page", defaultValue = "0")
            int pageNumber,

            @Parameter(description = "Page size of the paginated result",
                    example = "3")
            @RequestParam(value = "size", defaultValue = "10")
            int size
    ) {
        PageDTO<topGamesResponseDTO> games =
                gameService.findTopGames(pageNumber, size);
        return ResponseEntity.ok(games);
    }

    @PostMapping("/resync")
    @Operation(summary = "Update database",
            description = "Calls a routine to update the " +
                    "database with data fetched from RAWG API. " +
                    "**Requires admin privileges.")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Object> resyncWithExternalApi() {
        gameService.resyncAllGames(4);
        return ResponseEntity.ok().build();
    }
}
