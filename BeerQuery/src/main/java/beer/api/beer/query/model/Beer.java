package beer.api.beer.query.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Beer {
    @Id
    private String id;
    private String name;
    private String brewery;
    private String type;
    private String image;
    private String description;
    private float abv;
    private String country_iso;
}
