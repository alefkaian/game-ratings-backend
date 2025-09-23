package alef.gameRatingsAPI.entities.game;

import alef.gameRatingsAPI.shared.dto.PageDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/top")
    public ResponseEntity<PageDTO<topGamesDTO>> listTopGames(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageDTO<topGamesDTO> games = gameService.findTopGames(pageNumber, size);
        return ResponseEntity.ok(games);
    }

    @PostMapping("/resync")
    public ResponseEntity<Object> resyncWithExternalApi() {
        //gameService.testSingleFetch(null);
        gameService.resyncAllGames(4);
        return ResponseEntity.ok().build();
    }
}
