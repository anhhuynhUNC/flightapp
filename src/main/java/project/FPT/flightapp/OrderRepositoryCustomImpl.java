package project.FPT.flightapp;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.annotations.QueryHints;
import org.springframework.data.jpa.repository.Query;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<FlightOrder> customFindOrder(LocalDate date, Integer route) {
		// TODO Auto-generated method stub
		List<FlightDetail> flights = entityManager.createQuery(
				"SELECT distinct f FROM FlightDetail f LEFT JOIN FETCH f.airline LEFT JOIN FETCH f.route WHERE f.route.routeId = :route",
				FlightDetail.class).setParameter("route", route).setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
				.getResultList();

		List<FlightOrder> result = entityManager.createQuery(
				"SELECT distinct o FROM FlightOrder o LEFT JOIN FETCH o.outFlight LEFT JOIN FETCH o.returnFlight WHERE o.outFlight in :flights",
				FlightOrder.class).setParameter("flights", flights).setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
				.getResultList();

		return result.stream().filter((a) -> {
			return a.getDayOfFlight().getDay().isEqual(date);
		}).collect(Collectors.toList());

	}

	@Override
	public List<FlightOrder> customFindOrder(LocalDate date, Integer route, Integer ticket) {
		// TODO Auto-generated method stub
		List<FlightDetail> flights = entityManager.createQuery(
				"SELECT distinct f FROM FlightDetail f LEFT JOIN FETCH f.airline LEFT JOIN FETCH f.route WHERE f.route.routeId = :route",
				FlightDetail.class).setParameter("route", route).setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
				.getResultList();

		List<FlightOrder> result = entityManager.createQuery(
				"SELECT distinct o FROM FlightOrder o LEFT JOIN FETCH o.outFlight LEFT JOIN FETCH o.returnFlight WHERE o.outFlight in :flights",
				FlightOrder.class).setParameter("flights", flights).setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
				.getResultList();

		return result.stream().filter((a) -> {
			return a.getDayOfFlight().getDay().isEqual(date) && a.getTicketId() == ticket;
		}).collect(Collectors.toList());
	}

//f.depature >= :time AND f.depature < :time2 AND 
	// setParameter("time",
	// date.atStartOfDay()).setParameter("time2",date.plusDays(1).atStartOfDay())
}
