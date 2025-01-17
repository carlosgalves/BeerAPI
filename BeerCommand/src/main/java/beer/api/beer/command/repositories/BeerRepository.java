package beer.api.beer.command.repositories;


import beer.api.beer.command.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, String> {
}