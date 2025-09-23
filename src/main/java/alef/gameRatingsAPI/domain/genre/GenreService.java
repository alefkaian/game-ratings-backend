package alef.gameRatingsAPI.domain.genre;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional
    public Genre findOrCreateGenre(GenreDTO genreInfo) {
        if (genreInfo == null) return null;

        return genreRepository.findById(genreInfo.id()).orElseGet(() -> {
            Genre genre = new Genre(genreInfo.id(), genreInfo.slug(),
                    genreInfo.name(), genreInfo.image_background());
            return genreRepository.save(genre);
        });
    }

    public Set<Genre> getGenresList(List<GenreDTO> genreInfo) {
        if (genreInfo == null || genreInfo.isEmpty())
            return Collections.emptySet();
        return genreInfo.stream()
                .map(this::findOrCreateGenre)
                .collect(Collectors.toSet());
    }
}
