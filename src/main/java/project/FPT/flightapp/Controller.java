package project.FPT.flightapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = "/flight")
public class Controller {
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private RouteRepository routeRepo;
	@Autowired
	private AirlineRepository airRepo;
	@Autowired
	private TicketRepository ticketRepo;
	@Autowired
	private FlightDetailRepository fliDeRepo;
	@Autowired
	private MonthRepository monthRepo;
	@Autowired
	private DayRepository dayRepo;
	@Autowired
	private FlightOrderRepository orderRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private FlightService flightsev;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private DTOService DTOserv;
	@Autowired
	private OrderRepository orderCustomRepo;

	public Controller() {
	};

	/*
	 * Rest services for other resources
	 * 
	 */
	@GetMapping(path = "airline/all")
	public Iterable<Airline> getAirline() {
		return airRepo.findAll();
	}

	@GetMapping(path = "route/find")
	public String getRouteName(@RequestParam Integer id) {
		Route route = routeRepo.findById(id).get();
		City target = routeRepo.findById(id).get().getDepart();
		City source = routeRepo.findById(id).get().getArrive();

		return target.getName() + " to " + source.getName() + " as: " + route.getName();
	}

	@GetMapping(path = "route/all")
	public @ResponseBody Iterable<Route> getRoute() {
		return routeRepo.findAll();
	}

	@GetMapping(path = "/city/route/outbound")
	public @ResponseBody List<Route> getOutboundRoute(@RequestParam int id) {

		return cityRepository.findById(id).get().getDepartRoutes();
	}

	@GetMapping(path = "/city/route/inbound")
	public @ResponseBody List<Route> getInboundRoute(@RequestParam int id) {
		return cityRepository.findById(id).get().getArriveRoutes();
	}

	@GetMapping(path = "/city/all")
	public @ResponseBody Iterable<City> getAllCities() {
		return cityRepository.findAll();
	}

	@GetMapping(path = "/flights/all")
	public @ResponseBody Iterable<FlightDetail> getAllFlights() {
		return fliDeRepo.findAll();
	}
	
	@GetMapping(path = "/months/all")
	public @ResponseBody Iterable<Month> getMonths() {
		return monthRepo.findAll();
	}

	@GetMapping(path = "/days/all")
	public @ResponseBody Iterable<Day> getDays() {
		return dayRepo.findAll();
	}

	@GetMapping(path = "ticket/all")
	public @ResponseBody Iterable<Ticket> getTicket() {
		return flightsev.getAllTicketTypes();
	}
	/*
	 * Main rest Services for application.
	 * This is where displays will be fetched.
	 * 
	 */
	@GetMapping(path = "/display")
	public @ResponseBody List<FlightOrderDTO> getDisplay(@RequestParam String depart, @RequestParam String arrive, @RequestParam Integer ticket) {
		return flightsev.getFlightsConfigCustom(depart, arrive, ticket);
	}

	@GetMapping(path = "/maxDisplay")
	public @ResponseBody List<FlightOrderDTO> getMax(@RequestParam String depart, @RequestParam String arrive,  @RequestParam Integer ticket) {
		return flightsev.getFlightsConfigCustomMax(depart, arrive, ticket);
	}
	
	
	@GetMapping(path = "/order/byDay/s")
	public @ResponseBody List<FlightOrderDTO> getFlightsByDay(@RequestParam int id, @RequestParam String depart,
			@RequestParam String arrive, @RequestParam Integer ticket) {
		return flightsev.getAsSortedDTO(flightsev.getFlightsFromDay(id, depart, arrive, ticket).map(flightsev::convertToDTO)
				.collect(Collectors.toList()));
	}

	@GetMapping(path = "/order/all/sorted/p")
	public @ResponseBody List<FlightOrderDTO> getAllSorted(@RequestParam String depart, @RequestParam String arrive, @RequestParam Integer ticket) {
		// map flightorders to dto then sort
		Stream<FlightOrder> flights = flightsev.getFlights(depart, arrive, ticket);
		if (flights == null)
			return null;
		else
			return flightsev.getAsSortedDTO(flights.map(flightsev::convertToDTO).collect(Collectors.toList()));
	}

	@GetMapping(path = "/order/all/sorted/d")
	public @ResponseBody List<FlightOrderDTO> getAllSortedDate(@RequestParam String depart, @RequestParam String arrive, @RequestParam Integer ticket) {
		Stream<FlightOrder> flights = flightsev.getFlights(depart, arrive, ticket);
		if (flights == null)
			return null;
		else
			return flightsev.getAsSortedDateDTO(flights.map(flightsev::convertToDTO).collect(Collectors.toList()));
	}

	@GetMapping(path = "order/byMonth")
	public @ResponseBody List<FlightOrderDTO> getFlightsByMonth(@RequestParam Integer id, @RequestParam String depart, @RequestParam String arrive, @RequestParam boolean sort, @RequestParam Integer ticket) throws IllegalArgumentException {
		if (this.monthRepo.findById(id).get() == null) {
			throw new IllegalArgumentException("Invalid Month");
		}
		Stream <FlightOrder> flights = this.flightsev.getFlightsFromMonth(id, depart, arrive,ticket);
		if (flights ==  null) return null;
		try
		{
		    Thread.sleep(5000);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
		
		if(sort) return flightsev.getAsSortedDTO(flights.map(flightsev::convertToDTO).collect(Collectors.toList()));
		else return flights.map(flightsev::convertToDTO).collect(Collectors.toList());
	}
	
	/*
	 * Other services for retrieval of flight orders.
	 * retrieving all flight orders or sorting.
	 */

	@GetMapping(path = "/order/all")
	public @ResponseBody List<FlightOrder> getAllFlightOrders() {
		return orderRepo.findAll();
	}
	
	@GetMapping(path = "/order/all/fetch")
	public @ResponseBody List<FlightOrder> getAllFlightOrdersFetch() {
		return orderRepo.findAllQuery();
	}

	@GetMapping(path = "/order/all/byInt")
	public @ResponseBody List<FlightOrderDTO> getAllFlightOrdersDTO(Integer depart, Integer arrive) {
		return orderRepo.findByFlight(depart, arrive).stream().map(flightsev::convertToDTO).collect(Collectors.toList());
	}
	
	@GetMapping(path = "/order/all/byCode")
	public @ResponseBody List<FlightOrderDTO> getAllFlightOrdersDTO(String depart, String arrive) {
		return orderRepo.findByVna(depart, arrive).stream().map(flightsev::convertToDTO).collect(Collectors.toList());
	}
	
	@GetMapping(path = "order/all/sorted")
	public @ResponseBody List<FlightOrder> getAllSorted() {
		return flightsev.getAsSorted(orderRepo.findAll());
	}

	/*
	 * Miscellaneous services.
	 * 
	 */

	@GetMapping(path = "months/find")
	public @ResponseBody List<Day> getDaysFromMonth(@RequestParam int val) {
		return flightsev.getFullFromMonth(val);
	}

	@GetMapping(path = "/currentTime")
	public @ResponseBody LocalDateTime getTime() {
		return LocalDateTime.now();
	}

	// external api

	@GetMapping(path = "/vna/groups")
	public @ResponseBody List<VnaGroup> getGroup() {
		String url = "https://www.vietnamairlines.com/Json/GroupLocationFromTo-vi-VN.json";
		VnaCollection response = restTemplate.getForObject(url, VnaCollection.class);

		return response.getGroup();
	}

	@GetMapping(path = "/vna/airports")
	public String getVnaAirports() {
		String url = "https://www.vietnamairlines.com/Json/GroupLocationFromTo-vi-VN.json";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		return response.getBody();
	}

	
	/**
	 * Function to randomly generate flight orders on every day in the days table from the database ( as well as all routes ).
	 * Each day has a 50% chance to have flights, every price is random from 0 - 7000 USD.
	 * 
	 * @param: the type of ticket to populate flights. Currently only 4 available
	 * 
	 * For demonstration purposes only, DO NOT USE!!
	 *
	 **/
	@PostMapping(path = "/vna/flightDetail/add")

	public int addFlightDetail(@RequestParam Integer ticketId) {
		List<FlightDetail> flightDetailList = new ArrayList<>();
		List<FlightOrder> flightOrderList = new ArrayList<>();
		Iterator<Route> routeList = routeRepo.findAll().iterator();
		List<Day> dayList = dayRepo.findAll().stream().collect(Collectors.toList());
		Airline air = airRepo.findById(10).get();
		Ticket ticket = ticketRepo.findById(ticketId).get();

		while (routeList.hasNext()) {
			Route r = routeList.next();
			// Route r = routeRepo.findRouteByDepartAndArrive(20535, 20737);
			for (Day current : dayList) {
				double randomVar = Math.random();
				if (randomVar >= 0.5) {
					try {
						for (int i = 0; i < 3; i++) {
							FlightDetail flight = new FlightDetail();
							flight.setName(r.getName());
							flight.setAircraft("Airbus A350");
							flight.setRoute(r);
							flight.setAirline(air);
							flight.setArrivalGate(null);
							flight.setDepartGate(null);

							LocalDateTime a = LocalDateTime.of(current.getDay(), LocalTime.of(i + 10, 0, 0));
							LocalDateTime b = LocalDateTime.of(current.getDay().plusDays(2),
									LocalTime.of(i + 15, 0, 0));

							flight.setDeparture(a);
							flight.setArrival(b);


							FlightOrder flightOrder = new FlightOrder();
							flightOrder.setCustomer("Random");
							flightOrder.setDayOfFlight(current);
							flightOrder.setOutFlight(flight);
							flightOrder.setReturnFlight(null);
							flightOrder.setTicket(ticket);

							double price = Math.random() * 7000;
							flightOrder.setPrice(price);
							flightDetailList.add(flight);
							flightOrderList.add(flightOrder);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
		orderRepo.saveAll(flightOrderList);

		return flightOrderList.size();
	}
	
	@PostMapping(path = "/vna/flightDetail/add2")
	public int addFlightOrder() {
		Iterable<FlightDetail> details = fliDeRepo.findAll();
		Iterator<FlightDetail> iter = details.iterator();
		List<FlightOrder> orders = new ArrayList<>();
		List<Day> dayList = dayRepo.findAll().stream().collect(Collectors.toList());
		Ticket ticket = ticketRepo.findById(1).get();

		while(iter.hasNext()) {
			FlightDetail curr = iter.next();
			FlightOrder flightOrder = new FlightOrder();
			flightOrder.setCustomer("Random");

			Day day = null;
			LocalDate timeTemp = curr.getDeparture().toLocalDate();
			for(Day d : dayList) {
				if (timeTemp.isEqual(d.getDay())) day = d;
			}
			
			flightOrder.setDayOfFlight(day);
			flightOrder.setOutFlight(curr);
			flightOrder.setReturnFlight(null);
			flightOrder.setTicket(ticket);
			orders.add(flightOrder);
			
		}
		
		return orders.size();
		
	}

	@GetMapping(path = "/test")
	public List<FlightOrderDTO> tryM(@RequestParam String depart, @RequestParam String arrive, @RequestParam Integer ticket) {
		return flightsev.getFlightsConfigCustom(depart, arrive, ticket);
	}
	
	
	/*
	 * Internal tools, for interacting with database
	 * Mostly POST and DELETE requests
	 * 
	 */
	
	@PostMapping(path = "route/add")
	public String addRoute(@RequestBody Route route) throws IllegalArgumentException {
		if (cityRepository.existsByCityId(route.getArrive().getCityId())
				&& cityRepository.existsByCityId(route.getDepart().getCityId())) {
			routeRepo.save(route);
		} else
			throw new IllegalArgumentException("Record does not exist");

		return "All Done!";
	}

	@PostMapping(path = "/city/add")
	public @ResponseBody City addCity(@RequestBody City city) {
		cityRepository.save(city);

		return city;

	}

	@PostMapping(path = "city/remove")
	public @ResponseBody Iterable<City> removeCity(@RequestParam int id) {
		cityRepository.deleteById(id);

		return cityRepository.findAll();

	}
	
	@PutMapping(path = "airline/update")
	public Iterable<Airline> updateAirline(@RequestParam String code, @RequestParam Integer Id) {
		Airline air = airRepo.findById(Id).get();

		air.setCode(code);
		airRepo.save(air);
		return airRepo.findAll();

	}

	@PostMapping(path = "airline/add")
	public Iterable<Airline> addAirline(@RequestParam String name, @RequestParam String code) {
		Airline airline = new Airline();
		airline.setName(name);
		airline.setCode(code);
		airRepo.save(airline);

		return airRepo.findAll();

	}
	
	@PostMapping("/vna/routes")
	public String getVNAroutes() {
		String url = "https://www.vietnamairlines.com/Json/GroupLocationFromTo-vi-VN.json";
		VnaCollection response = restTemplate.getForObject(url, VnaCollection.class);

		List<VnaAirports> vnacities = response.getAirport();
		List<City> cities = new ArrayList<City>();
		for (VnaAirports vna : vnacities) {
			City temp = new City();
			temp.setAirport(vna.getCode());
			temp.setName(vna.getDisplayName());
			temp.setVnaCode(vna.getId());
			temp.setArriveRoutes(null);
			temp.setDepartRoutes(null);
			cities.add(temp);
		}

		ResponseEntity<String> object = restTemplate.postForEntity("http://localhost:8080/flight/vna/city/add", cities,
				String.class);

		return object.getBody();
	}

	@PostMapping(path = "/vna/city/add")
	public @ResponseBody String addVnaCity(@RequestBody List<City> cities) {
		for (City city : cities) {
			cityRepository.save(city);
		}

		return "Done";

	}
	
	 

}
