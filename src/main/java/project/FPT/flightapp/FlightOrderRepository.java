package project.FPT.flightapp;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.FPT.flightapp.FlightOrder;

public interface FlightOrderRepository extends JpaRepository<FlightOrder, Integer> {
	// boolean existsByRouteID(Integer routeId);
	@Query(value = "SELECT * FROM flightorder o " + "INNER JOIN flightdetail d ON o.OutFlight = d.FlightId "
			+ "INNER JOIN route r ON r.RouteID = d.RouteId "
			+ "WHERE r.DepartCity = :depart AND r.ArriveCity = :arrive", nativeQuery = true)
	List<FlightOrder> findByFlight(@Param("depart") Integer depart, @Param("arrive") Integer arrive);

	@Query(value = "SELECT * FROM flightorder o INNER JOIN flightdetail d ON o.OutFlight = d.FlightId "
			+ "INNER JOIN route r ON r.RouteID = d.RouteId " + "INNER JOIN city c1 ON c1.cityId = r.DepartCity "
			+ "INNER JOIN city c2 ON c2.cityId = r.ArriveCity "
			+ "WHERE c1.Airport = :depart AND c2.Airport = :arrive", nativeQuery = true)
	List<FlightOrder> findByVna(@Param("depart") String depart, @Param("arrive") String arrive);

	@Query(value = "SELECT o FROM FlightOrder o LEFT JOIN FETCH o.outFlight LEFT JOIN FETCH o.returnFlight LEFT JOIN FETCH o.dayOfFlight LEFT JOIN FETCH o.ticket")
	List<FlightOrder> findAllQuery();

	@EntityGraph(type = EntityGraphType.FETCH, attributePaths = { "outFlight", "returnFlight", "dayOfFlight", "ticket",
			"outFlight.route", "outFlight.airline", "returnFlight.route", "returnFlight.airline",
			"outFlight.route.depart", "outFlight.route.arrive", "returnFlight.route.depart",
			"returnFlight.route.arrive" })
	List<FlightOrder> findAll();

		@Query(value = "select tbl.* from flightorder tbl INNER JOIN "
				+ "flightdetail f1 on f1.FlightId = tbl.OutFlight INNER JOIN "
				+ "(SELECT o.OrderId,Min(o.Price) minPrice, o.OutFlight, o.DateId FROM flightOrder o inner join flightdetail f on f.FlightId = o.OutFlight where f.RouteId = :route and o.ticketId = :ticket GROUP BY o.dateId) tbl1 "
				+ "on tbl.DateId = tbl1.DateId where tbl.Price = tbl1.minPrice AND f1.RouteId = :route AND f1.DepartureTime >= :time AND TicketId = :ticket ORDER BY tbl.DateId", nativeQuery = true)
	List<FlightOrder> customMinFind(@Param("time") LocalDateTime time, @Param("route") Integer route, @Param("ticket") Integer ticket);
	
	@Query(value = "select tbl.* from flightorder tbl INNER JOIN "
			+ "flightdetail f1 on f1.FlightId = tbl.OutFlight INNER JOIN "
			+ "(SELECT o.OrderId,Max(o.Price) minPrice, o.OutFlight, o.DateId FROM flightOrder o inner join flightdetail f on f.FlightId = o.OutFlight where f.RouteId = :route and o.ticketId = :ticket GROUP BY o.dateId) tbl1 "
			+ "on tbl.DateId = tbl1.DateId where tbl.Price = tbl1.minPrice AND f1.RouteId = :route AND f1.DepartureTime >= :time AND TicketId = :ticket ORDER BY tbl.DateId", nativeQuery = true)
	List<FlightOrder> customMaxFind(@Param("time") LocalDateTime time, @Param("route") Integer route, @Param("ticket") Integer ticket);

}

//"SELECT o FROM FlightOrder o LEFT JOIN FETCH o.outFlight LEFT JOIN FETCH o.returnFlight LEFT JOIN FETCH o.dayOfFlight LEFT JOIN FETCH o.ticket LEFT JOIN Route r LEFT JOIN FETCH  "

/*
 * value =
 * "select tbl from FlightOrder tbl LEFT JOIN FETCH tbl.dayOfFlight LEFT JOIN FETCH "
 * + "tbl.outFlight where tbl.price = " +
 * "(SELECT Min(o.price) FROM FlightOrder o INNER JOIN o.outFlight where o.outFlight.route.routeId = 19 AND o.outFlight = tbl.outFlight)"
 */