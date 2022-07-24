package project.FPT.flightapp;

import java.awt.SystemColor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ser.std.IterableSerializer;

/**
 * @author anh
 *
 */
@Service
public class FlightService {
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private RouteRepository routeRepo;
	@Autowired
	private FlightDetailRepository fliDeRepo;
	@Autowired
	private TicketRepository tickRepo;
	@Autowired
	private MonthRepository monthRepo;
	@Autowired
	private DayRepository dayRepo;
	@Autowired
	private FlightOrderRepository orderRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private OrderRepository orderCustomRepo;

	private String test;
	private List<FlightOrderDTO> displayList = new ArrayList<FlightOrderDTO>();

	/*
	 * Services for retrieval Cities / Flights / Tickets
	 */

	public Iterable<City> getAllCities() {
		return cityRepository.findAll();
	}

	public Iterable<FlightDetail> getAllFlights() {
		return fliDeRepo.findAll();
	}

	public Iterable<Ticket> getAllTicketTypes() {
		return tickRepo.findAll();
	}

	public Iterable<Month> getMonths() {
		return monthRepo.findAll();
	}

	public Iterable<Day> getDays() {
		return dayRepo.findAll();
	}

	/*
	 * Services for retrieval of flights from certain conditions Important for
	 * setting up the necessary displays for application
	 * 
	 */

	/**
	 * Retrieve all the flights from the route specified by the input destination
	 * and arrival, as well as the type of ticket.
	 * 
	 * @param depart : the Departure airport represented by the VNA code (e.g. "HAN"
	 *               or "SGN")
	 * @param arrive : the Arrival airport represented by the VNA code (e.g. "HAN"
	 *               or "SGN")
	 * @param ticket : the type of ticket
	 * @return The list of flight DTO, only populated by the cheapest flight in the
	 *         day.
	 */
	public List<FlightOrderDTO> getFlightsConfigCustom(String depart, String arrive, Integer ticket) {
		// testing index
		int index = 0;
		Route route = this.routeRepo.findRouteByDepartAndArrive(depart, arrive);
		if (route == null) {
			return null;
		}

		LocalDate currentTime = LocalDate.now();

		Iterable<FlightOrder> iterFlights = orderRepo.customMinFind(currentTime.atStartOfDay(),
				this.routeRepo.findRouteByDepartAndArrive(depart, arrive).getRouteId(), ticket);

		if (iterFlights == null || ((List<FlightOrder>) iterFlights).size() == 0)
			return null;

		Iterator<FlightOrder> iter = iterFlights.iterator();

		List<FlightOrderDTO> result = new ArrayList<>();
		List<Month> months = monthRepo.findAll();

		FlightOrder curr = iter.next();

		// can change based on need - for now until next six months
		for (Month m : months) {
			if (m.getValue() < currentTime.getMonthValue() && m.getYear() == currentTime.getYear())
				continue;
			if (m.getValue() >= currentTime.plusMonths(6).getMonthValue()
					&& m.getYear() >= currentTime.plusMonths(6).getYear())
				break; // break if more than 6 months

			List<Day> days = this.getFullFromMonth(m.getValue(), m.getYear());

			for (Day day : days) {
				if (day.getDayOfYear() < currentTime.getDayOfYear() && day.getDay().getYear() == currentTime.getYear())
					continue; // skip irrelevant days at start of month
				if (!curr.getDayOfFlight().getDay().isEqual(day.getDay())) {
					FlightOrderDTO blank = new FlightOrderDTO();
					blank.setDayOfFlight(day); // blank.set
					result.add(blank);
				} else {
					result.add(convertToDTO(curr));
					index++;
					while (iter.hasNext()) {
						FlightOrder next = iter.next();
						// handle edge cases where two flights on the same day has the same minimum
						// price
						if (!curr.getDayOfFlight().getDay().isEqual(next.getDayOfFlight().getDay())) {
							// only first instance of day is displayed
							curr = next;
							break;
						}

					}

				}
			}
		}

		return result;
	}

	/**
	 * Retrieve all the flights from the route specified by the input destination
	 * and arrival, as well as the type of ticket.
	 * 
	 * @param depart : the Departure airport represented by the VNA code (e.g. "HAN"
	 *               or "SGN)
	 * @param arrive : the Arrival airport represented by the VNA code (e.g. "HAN"
	 *               or "SGN)
	 * @param ticket : the type of ticket
	 * @return The list of flight DTO, only populated by the most expensive flight
	 *         in the day.
	 */
	public List<FlightOrderDTO> getFlightsConfigCustomMax(String depart, String arrive, Integer ticket) {
		// testing index
		int index = 0;
		Route route = this.routeRepo.findRouteByDepartAndArrive(depart, arrive);
		if (route == null) {
			return null;
		}

		LocalDate currentTime = LocalDate.now();

		Iterable<FlightOrder> iterFlights = orderRepo.customMaxFind(currentTime.atStartOfDay(),
				this.routeRepo.findRouteByDepartAndArrive(depart, arrive).getRouteId(), ticket);

		if (iterFlights == null || ((List<FlightOrder>) iterFlights).size() == 0)
			return null;

		Iterator<FlightOrder> iter = iterFlights.iterator();

		List<FlightOrderDTO> result = new ArrayList<>();
		List<Month> months = monthRepo.findAll();

		FlightOrder curr = iter.next();

		// can change based on need - for now until next year
		for (Month m : months) {
			if (m.getValue() < currentTime.getMonthValue() && m.getYear() == currentTime.getYear())
				continue;
			if (m.getValue() >= currentTime.plusMonths(6).getMonthValue()
					&& m.getYear() >= currentTime.plusMonths(6).getYear())
				break; // break if more than 6 months

			List<Day> days = this.getFullFromMonth(m.getValue(), m.getYear());

			for (Day day : days) {
				if (day.getDayOfYear() < currentTime.getDayOfYear() && day.getDay().getYear() == currentTime.getYear())
					continue; // skip irrelevant days at start of month

				if (!curr.getDayOfFlight().getDay().isEqual(day.getDay())) {
					FlightOrderDTO blank = new FlightOrderDTO();
					blank.setDayOfFlight(day); // blank.set
					result.add(blank);
				} else {
					result.add(convertToDTO(curr));
					index++;
					while (iter.hasNext()) {
						FlightOrder next = iter.next();
						// handle edge cases where two flights on the same day has the same minimum
						// price
						if (!curr.getDayOfFlight().getDay().isEqual(next.getDayOfFlight().getDay())) {
							// only first instance of day is displayed
							curr = next;
							break;
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Retrieve all flights of a date, using custom fetch method
	 * 
	 * @param id     the id of the date
	 * @param depart the departure airport in code
	 * @param arrive the arrival airport in code
	 * @param ticket the ticket id
	 * @return a stream of FlightOrder objects
	 */
	public Stream<FlightOrder> getFlightsFromDay(int id, String depart, String arrive, Integer ticket) {
		// convert to Stream and compare day of year (for testing)
		LocalDate date = dayRepo.findById(id).get().getDay();
		Integer routeId = routeRepo.findRouteByDepartAndArrive(depart, arrive).getRouteId();

		return orderCustomRepo.customFindOrder(date, routeId, ticket).stream();
	}

	/**
	 * Retrieve all flights of a month, using custom fetch method
	 * 
	 * @param id     the id of the month
	 * @param depart the departure airport in code
	 * @param arrive the arrival airport in code
	 * @param ticket the ticket id
	 * @return a stream of FlightOrder objects
	 */
	public Stream<FlightOrder> getFlightsFromMonth(Integer id, String depart, String arrive, Integer ticket) {
		if (routeRepo.findRouteByDepartAndArrive(depart, arrive) == null)
			return null;

		LocalDate currentTime = LocalDate.now();
		List<FlightOrder> flights = this.orderRepo.findByVna(depart, arrive);

		Stream<FlightOrder> result = flights.stream().filter((a) -> {
			return compareDate(a.getDayOfFlight().getDay(), currentTime)
					&& a.getDayOfFlight().getMonth().getMonthId() == id && a.getTicketId() == ticket;
		});

		return result;
	}

	/**
	 * Filter the current flightorders with params
	 * 
	 * @param depart
	 * @param arrive
	 * @return FlightOrders that match the route
	 */
	public Stream<FlightOrder> getFlights(String depart, String arrive, Integer ticket) {
		if (routeRepo.findRouteByDepartAndArrive(depart, arrive) == null)
			return null;

		LocalDate currentTime = LocalDate.now();
		List<FlightOrder> flights = this.orderRepo.findByVna(depart, arrive);

		Stream<FlightOrder> result = flights.stream().filter((a) -> {
			return compareDate(a.getDayOfFlight().getDay(), currentTime) && a.getTicketId() == ticket;
		});

		return result;
	}

	public List<Day> getFullFromMonth(int val) {
		return monthRepo.findByValue(val).getDays();
	}

	public List<Day> getFullFromMonth(int val, int year) {
		return monthRepo.findByBoth(val, year).getDays();
	}

	// VNA services
	public VnaCollection getVnaCollection() {
		String url = "https://www.vietnamairlines.com/Json/GroupLocationFromTo-vi-VN.json";
		VnaCollection response = restTemplate.getForObject(url, VnaCollection.class);

		return response;
	}

	public List<Route> getList() {
		List<Route> result = new ArrayList<>();
		List<VnaGroup> collection = this.getVnaCollection().getGroup();
		for (VnaGroup group : collection) {
			List<VnaGroupAirport> listGroup = group.getAirports();
			for (VnaGroupAirport groupAir : listGroup) {
				String name = groupAir.getCode();
				List<VnaGroup> smallestGroup = groupAir.getDlGroups();
				City depart = this.cityRepository.findByAirport(name);
				for (VnaGroup g : smallestGroup) {
					String subsubString = "";
					List<VnaGroupAirport> smallestAirportGroup = g.getAirports();
					for (VnaGroupAirport a : smallestAirportGroup) {
						City arrive = this.cityRepository.findByAirport(a.getCode());
						Route r = new Route();
						r.setArrive(arrive);
						r.setDepart(depart);
						r.setName(depart.getAirport() + "-" + arrive.getAirport());
						result.add(r);
					}

				}
			}
		}

		return result;
	}

	// Sorting

	public Heap getAsHeap(List<FlightOrder> flightOrders) {
		MinBinHeap heap = new MinBinHeap(flightOrders.size() + 1);

		for (FlightOrder fo : flightOrders) {
			heap.insert(fo);
		}

		return heap;

	}

	public Heap getAsHeap(List<FlightOrder> flightOrders, String depart, String arrive) {
		MinBinHeap heap = new MinBinHeap(flightOrders.size() + 1);

		for (FlightOrder fo : flightOrders) {
			Route r = fo.getOutFlight().getRoute();
			if (r.getDepartureCityAirport() == depart && r.getArriveCityAirport() == arrive)
				heap.insert(fo);
		}

		return heap;
	}

	/**
	 * @param orders the list of flight orders
	 * @return sorted list of DTOs by date, ascending
	 */
	public List<FlightOrderDTO> getAsSortedDateDTO(List<FlightOrderDTO> orders) {
		Collections.sort(orders, new Comparator<FlightOrderDTO>() {
			@Override
			public int compare(FlightOrderDTO o1, FlightOrderDTO o2) {
				// TODO Auto-generated method stub
				return o1.getDayOfFlight().getDay().compareTo(o2.getDayOfFlight().getDay());
			}
		});
		return orders;

	}

	/**
	 * @param order the list of flight orders
	 * @return sorted list by price, ascending
	 */
	public List<FlightOrder> getAsSorted(List<FlightOrder> orders) {
		Collections.sort(orders, new Comparator<FlightOrder>() {
			@Override
			public int compare(FlightOrder o1, FlightOrder o2) {
				// TODO Auto-generated method stub
				return (int) (o1.getPrice() - o2.getPrice());
			}
		});
		return orders;
	}

	/**
	 * @param orders the list of flight orders DTO
	 * @return sorted list by price, ascending
	 */
	public List<FlightOrderDTO> getAsSortedDTO(List<FlightOrderDTO> orders) {
		Collections.sort(orders, new Comparator<FlightOrderDTO>() {

			@Override
			public int compare(FlightOrderDTO o1, FlightOrderDTO o2) { // TODO
				// Auto-generated method stub
				// if(o1.getPrice())
				return (int) (o1.getPrice() - o2.getPrice());
			}
		});
		return orders;
	}

	// DTO conversions

	public FlightOrderDTO convertToDTO(FlightOrder order) {
		FlightOrderDTO orderDTO = modelMapper.map(order, FlightOrderDTO.class);
		return orderDTO;
	}

	// Helper
	public boolean compareRoute(FlightOrder a, String depart, String arrive) {
		Route r = a.getOutFlight().getRoute();
		return r.getDepartureCityAirport() == depart && r.getArriveCityAirport() == arrive;
	}

	public boolean compareDate(LocalDate a, LocalDate b) {
		if (a.compareTo(b) >= 0) {
			return true;
		} else {
			return false;
		}
	}
}
