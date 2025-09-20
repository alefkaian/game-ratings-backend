package alef.gameRatingsAPI.entities.genre;

import alef.gameRatingsAPI.entities.game.Game;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "genres")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
public class Genre {
    @Id
    @EqualsAndHashCode.Include
    private long id;
    private String slug;
    private String name;
    @Column(name = "background_image")
    private String backgroundImage;
    @ManyToMany(mappedBy = "genres")
    private Set<Game> games;

    public Genre(long id, String slug, String name, String backgroundImage) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.backgroundImage = backgroundImage;
    }
}
