package alef.gameRatingsAPI.entities.esrbRating;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "esrb_ratings")
@Getter
@Setter
@NoArgsConstructor
public class EsrbRating {
    @Id
    private long id;
    private String slug;
    private String name;

    public EsrbRating(long id, String slug, String name) {
        this.id = id;
        this.slug = slug;
        this.name = name;
    }
}
