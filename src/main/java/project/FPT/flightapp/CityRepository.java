package project.FPT.flightapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import project.FPT.flightapp.City;

public interface CityRepository extends JpaRepository<City, Integer> {
	boolean existsByCityId(Integer cityId);
	City findByAirport(String code);
}
