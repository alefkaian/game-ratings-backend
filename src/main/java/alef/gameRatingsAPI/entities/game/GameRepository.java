package alef.gameRatingsAPI.entities.game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g ORDER BY (g.metacritic * 0.5 + g.rating * 20" +
            " * 0.5) DESC")
    Page<Game> findTopGames(Pageable pageable);
}
