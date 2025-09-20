package alef.gameRatingsAPI.entities.esrbRating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EsrbRatingRepository extends JpaRepository<EsrbRating, Long> {
    Optional<EsrbRating> findBySlug(String slug);
}
