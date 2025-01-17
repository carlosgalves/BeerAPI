package beer.api.beer.query.model;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Beer {
    @Id
    private UUID id;
    private String name;
    private String brewery;
    private String type;
    private String image;
    private String description;
    private float abv;
    private String countryIso;
    private String ean;
    @ElementCollection
    private List<String> tags;
    private float overallRating;
    private float aromaRating;
    private float tasteRating;
    private float afterTasteRating;
}
