package project.FPT.flightapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "flightdetail")
public class FlightDetail {

	/*
	 * @Id
	 * 
	 * @GeneratedValue(generator = "generator")
	 * 
	 * @GenericGenerator(name = "generator", strategy = "increment")
	 * 
	 * @Column(name = "FlightId")
	 */

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	@Column(name = "FlightId")
	private Integer flightId;

	@Column(name = "FlightName")
	private String name;

	@Column(name = "AircraftType")
	private String aircraft;

	@Column(name = "DepartureTime")
	private LocalDateTime departure;

	@Column(name = "ArrivalTime")
	private LocalDateTime arrival;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RouteId")
	@JsonBackReference(value = "routeReference")
	private Route route;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AirlineId")
	@JsonBackReference(value = "airlineReference")
	private Airline airline;

	@Column(name = "DepartureGate")
	private String departGate;

	@Column(name = "ArrivalGate")
	private String arrivalGate;

	/*
	 * @JsonIgnore
	 * 
	 * @OneToMany(mappedBy = "outFlight", cascade = CascadeType.ALL, orphanRemoval =
	 * true)
	 * 
	 * @JsonManagedReference(value = "outflightReference")
	 * 
	 * @Fetch(value = FetchMode.SUBSELECT) private List<FlightOrder> outflightOrders
	 * = new ArrayList<>();
	 * 
	 * @JsonIgnore
	 * 
	 * @OneToMany(mappedBy = "returnFlight", cascade = CascadeType.ALL,
	 * orphanRemoval = true)
	 * 
	 * @JsonManagedReference(value = "returnflightReference")
	 * 
	 * @Fetch(value = FetchMode.SUBSELECT) private List<FlightOrder>
	 * returnflightOrders = new ArrayList<>();
	 */

	public Integer getFlightId() {
		return flightId;
	}

	public void setFlightId(Integer flightId) {
		this.flightId = flightId;
	}

	public Integer getRouteId() {
		return this.getRoute().getRouteId();
	}

	public Integer getAirlineId() {
		return this.getAirline().getAirlineId();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAircraft() {
		return aircraft;
	}

	public void setAircraft(String aircraft) {
		this.aircraft = aircraft;
	}

	public LocalDateTime getDeparture() {
		return departure;
	}

	public void setDeparture(LocalDateTime departure) {
		this.departure = departure;
	}

	public LocalDateTime getArrival() {
		return arrival;
	}

	public void setArrival(LocalDateTime arrival) {
		this.arrival = arrival;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Airline getAirline() {
		return airline;
	}

	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	public String getDepartGate() {
		return departGate;
	}

	public void setDepartGate(String departGate) {
		this.departGate = departGate;
	}

	public String getArrivalGate() {
		return arrivalGate;
	}

	public void setArrivalGate(String arrivalGate) {
		this.arrivalGate = arrivalGate;
	}

	/*
	 * public List<FlightOrder> getOutflightOrders() { return outflightOrders; }
	 * 
	 * public List<FlightOrder> getReturnflightOrders() { return returnflightOrders;
	 * }
	 */

}
