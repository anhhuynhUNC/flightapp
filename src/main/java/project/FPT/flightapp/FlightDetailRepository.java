package project.FPT.flightapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.FPT.flightapp.FlightDetail;

public interface FlightDetailRepository extends JpaRepository<FlightDetail,Integer>{
	//boolean existsByRouteID(Integer routeId);
	/*
	 * @Query(value = "SELECT * FROM Flightdetail f WHERE f.Route = :routeId",
	 * nativeQuery = true) Route findByRoute(@Param("routeId") Integer routeId);
	 */
}
