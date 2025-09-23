package alef.gameRatingsAPI.domain.game;

import alef.gameRatingsAPI.shared.dto.PageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/top")
    public ResponseEntity<PageDTO<topGamesResponseDTO>> listTopGames(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageDTO<topGamesResponseDTO> games = gameService.findTopGames(pageNumber, size);
        return ResponseEntity.ok(games);
    }

    @PostMapping("/resync")
    public ResponseEntity<Object> resyncWithExternalApi() {
        //gameService.testSingleFetch(null);
        gameService.resyncAllGames(4);
        return ResponseEntity.ok().build();
    }
}
