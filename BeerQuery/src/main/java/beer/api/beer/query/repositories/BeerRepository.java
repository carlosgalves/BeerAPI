package beer.api.beer.query.repositories;

import beer.api.beer.query.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, String> {
}