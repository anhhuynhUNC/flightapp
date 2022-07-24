package project.FPT.flightapp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends OrderRepositoryCustom, JpaRepository<FlightOrder, Integer>{

}
