package beer.api.beer.command.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
public class BeerUpdatedEvent {
    private String id;
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
