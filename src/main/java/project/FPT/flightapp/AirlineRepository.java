package project.FPT.flightapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import project.FPT.flightapp.Airline;

public interface AirlineRepository extends CrudRepository<Airline,Integer>{
	//boolean existsBy

}
