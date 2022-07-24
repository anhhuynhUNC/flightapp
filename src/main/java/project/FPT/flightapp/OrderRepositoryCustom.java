package project.FPT.flightapp;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepositoryCustom {
	public List<FlightOrder> customFindOrder(LocalDate date, Integer route);
	public List<FlightOrder> customFindOrder(LocalDate date, Integer route, Integer ticket);
	
}
