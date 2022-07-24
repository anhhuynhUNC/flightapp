package project.FPT.flightapp;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/*Generated entity for routes, contains departure and arrival cities
 *@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="routeId")
 * 
 * 
 */
@Entity
@Table(name = "route")
public class Route  {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RouteID")
	private Integer routeId;

	// @Column(name = "DepartCity")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DepartCity")
	@JsonBackReference(value = "departReference")
	@JsonInclude()
	private City depart;

	// @Column(name = "ArriveCity")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ArriveCity")
	@JsonBackReference(value = "arriveReference")
	private City arrive;

	/*
	 * // one to many to flight //Ignore flights listed
	 * 
	 * @JsonIgnore
	 * 
	 * @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval =
	 * true)
	 * 
	 * @Fetch(value = FetchMode.SUBSELECT)
	 * 
	 * @JsonManagedReference(value = "routeReference") private List<FlightDetail>
	 * flights = new ArrayList<>();
	 */
	@Column(name = "Name")
	private String name;

	public Route() {
	};

	public Integer getDepartureCityId() {
		return this.depart.getCityId();
	}

	public Integer getArriveCityId() {
		return this.arrive.getCityId();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public City getDepart() {
		return depart;
	}

	public void setDepart(City depart) {
		this.depart = depart;
	}

	public City getArrive() {
		return arrive;
	}

	public void setArrive(City arrive) {
		this.arrive = arrive;
	}
	
	/*
	 * @JsonIgnore public List<FlightDetail> getFlights() { return flights; }
	 * 
	 * public void setFlights(List<FlightDetail> flights) { this.flights = flights;
	 * }
	 */
	
	public String getDepartCityName() {
		return this.depart.getName();
	}

	public String getArriveCityName() {
		return this.arrive.getName();
	}
	
	public String getDepartureCityAirport() {
		return this.depart.getAirport();
	}

	public String getArriveCityAirport() {
		return this.arrive.getAirport();
	}
}
