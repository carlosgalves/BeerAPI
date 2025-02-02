package beer.api.beer.command.repositories;


import beer.api.beer.command.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
    List<Beer> id(UUID id);
    boolean existsByEan(String ean);
}