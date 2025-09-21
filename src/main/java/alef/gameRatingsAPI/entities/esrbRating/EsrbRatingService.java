package alef.gameRatingsAPI.entities.esrbRating;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EsrbRatingService {
    private final EsrbRatingRepository esrbRatingRepository;

    public EsrbRatingService(EsrbRatingRepository esrbRatingRepository) {
        this.esrbRatingRepository = esrbRatingRepository;
    }

    @Transactional
    public EsrbRating findOrCreateEsrbRating(EsrbRatingDTO esrbRatingInfo) {
        if (esrbRatingInfo == null) return null;
        return esrbRatingRepository.findById(esrbRatingInfo.id()).orElseGet(() -> {
            EsrbRating esrbRating = new EsrbRating(esrbRatingInfo.id(),
                    esrbRatingInfo.slug(), esrbRatingInfo.name());
            return esrbRatingRepository.save(esrbRating);
        });
    }
}
