package alef.gameRatingsAPI.domain.genre;

public record GenreNameDTO(String slug, String name) {
    public GenreNameDTO(Genre genre) {
        this(genre.getSlug(), genre.getName());
    }
}
