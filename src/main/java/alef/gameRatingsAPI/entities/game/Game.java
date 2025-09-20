package alef.gameRatingsAPI.entities.game;

import alef.gameRatingsAPI.entities.esrbRating.EsrbRating;
import alef.gameRatingsAPI.entities.genre.Genre;
import alef.gameRatingsAPI.entities.platform.Platform;
import alef.gameRatingsAPI.entities.publisher.Publisher;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "games")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
public class Game {
    @Id
    @EqualsAndHashCode.Include
    private long id;
    private String slug;
    private String name;
    private String description;
    private Boolean tba;
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @Column(name = "background_image")
    private String backgroundImage;
    private double rating;
    @Column(name = "top_rating")
    private double topRating;
    private int metacritic;
    @Column(name = "refresh_time")
    private LocalDate refreshTime;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "esrb_rating_id")
    private EsrbRating esrbRating;
    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "games_platforms", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "platform_id"))
    private Set<Platform> platforms;
    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "games_genres", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

}