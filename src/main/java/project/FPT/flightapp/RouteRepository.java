package project.FPT.flightapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import project.FPT.flightapp.Route;

public interface RouteRepository extends CrudRepository<Route, Integer> {
	// boolean existsByRouteID(Integer routeId);
	@Query(value = "SELECT * FROM route r INNER JOIN city c1 ON c1.cityId = r.DepartCity INNER JOIN city c2 ON c2.cityId = r.ArriveCity WHERE c1.Airport = :depart AND c2.Airport = :arrive", nativeQuery = true)
	Route findRouteByDepartAndArrive(@Param("depart") String depart, @Param("arrive") String arrive);
}
