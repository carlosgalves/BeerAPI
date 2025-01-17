package beer.api.beer.query.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeerCreatedEvent {
    private String id;
    private String name;
    private String brewery;
    private String type;
    private String image;
    private String description;
    private float abv;
    private String countryIso;
}