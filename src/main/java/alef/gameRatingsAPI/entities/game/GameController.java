package alef.gameRatingsAPI.entities.game;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/resync")
    public ResponseEntity<Object> testFetchGames() {
        //gameService.testSingleFetch(null);
        gameService.resyncAllGames(4);
        return ResponseEntity.ok().build();
    }
}
