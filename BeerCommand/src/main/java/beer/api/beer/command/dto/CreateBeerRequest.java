package beer.api.beer.command.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateBeerRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String brewery;
    @NotBlank
    private String type;
    private String image;
    private String description;
    @Positive
    private float abv;
    @NotBlank
    private String countryIso;
}