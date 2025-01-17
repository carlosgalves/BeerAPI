package beer.api.beer.command.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Beer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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
    //@NotBlank(message = "EAN cannot be blank")
    private String ean;

    @Column(nullable = false)
    @Min(value = 0, message = "Rating cannot be lower than 0")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private float overallRating;

    @Column(nullable = false)
    @Min(value = 0, message = "Rating cannot be lower than 0")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private float aromaRating;

    @Column(nullable = false)
    @Min(value = 0, message = "Rating cannot be lower than 0")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private float tasteRating;

    @Column(nullable = false)
    @Min(value = 0, message = "Rating cannot be lower than 0")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private float afterTasteRating;
}
