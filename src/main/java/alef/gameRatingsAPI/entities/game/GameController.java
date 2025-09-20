package alef.gameRatingsAPI.entities.game;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
    @RequestMapping("/games")
    public class GameController {
        private final GameService gameService;

        public GameController(GameService gameService){
            this.gameService = gameService;
        }

        @PostMapping("/test")
        public ResponseEntity<Object> testFetchGames() {
            //gameService.testSingleFetch(null);
            //return ResponseEntity.ok().build();
            gameService.fetchAndRefreshAllGames(null);
            return ResponseEntity.ok().build();
        }
    }
