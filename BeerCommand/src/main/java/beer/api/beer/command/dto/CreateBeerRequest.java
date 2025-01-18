package beer.api.beer.command.dto;


import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateBeerRequest {
    @Column(nullable = false)
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Brewery cannot be blank")
    private String brewery;

    @Column(nullable = false)
    @NotBlank(message = "Type cannot be blank")
    private String type;

    private String image;

    @Column(length = 100)
    @Size(max = 100, message = "Description cannot exceed 100 characters")
    private String description;

    @Column(nullable = false)
    @Min(value = 0, message = "ABV cannot be lower than 0")
    private float abv;

    @Pattern(regexp = "^[A-Za-z]{2}$", message = "Country ISO must consist of exactly two letters according to ISO-3166")
    private String countryIso;

    @ElementCollection
    @Column(name = "tag")
    private List<String> tags;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "EAN cannot be blank")
    private String ean;
}