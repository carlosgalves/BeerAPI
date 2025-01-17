package beer.api.beer.query.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeerCreatedEvent {
    private UUID id;
    private String name;
    private String brewery;
    private String type;
    private String image;
    private String description;
    private float abv;
    private String countryIso;
    private String ean;
    private List<String> tags;
    private float overallRating;
    private float aromaRating;
    private float tasteRating;
    private float afterTasteRating;
}