package alef.gameRatingsAPI.domain.platform;

import alef.gameRatingsAPI.domain.game.Game;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "platforms")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
public class Platform {
    @Id
    @EqualsAndHashCode.Include
    private long id;
    private String slug;
    private String name;
    @Column(name = "background_image")
    private String backgroundImage;
    @ManyToMany(mappedBy = "platforms")
    private Set<Game> games;

    public Platform(long id, String slug, String name, String backgroundImage) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.backgroundImage = backgroundImage;
    }
}
