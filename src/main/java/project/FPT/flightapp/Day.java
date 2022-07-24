package project.FPT.flightapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * <p>
 * Day class, represents available days associated with a month id
 * <p>
 * Useful for filtering through days of flights without needing to extract days
 * information
 * 
 */
@Entity
@Table(name = "day")
public class Day {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DateId")
	private Integer dateId;

	@Column(name = "Day")
	private LocalDate day;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MonthId")
	@JsonBackReference
	private Month month;

	/*
	 * @OneToMany(mappedBy = "dayOfFlight", cascade = CascadeType.ALL, orphanRemoval
	 * = true)
	 * 
	 * @Fetch(value = FetchMode.SUBSELECT)
	 * 
	 * @JsonManagedReference(value = "dayReference") private List<FlightOrder>
	 * flightsOnDay = new ArrayList<>();
	 */

	public Integer getDateId() {
		return dateId;
	}

	public void setDateId(Integer dateId) {
		this.dateId = dateId;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public int getDayOfMonth() {
		return this.day.getDayOfMonth();
	}

	public int getDayOfYear() {
		return this.day.getDayOfYear();
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public int getMonthValue() {
		return this.month.getValue();
	}
	/**
	 * @return the flightsOnDay
	 */
	/*
	 * public List<FlightOrder> getFlightsOnDay() { return flightsOnDay; }
	 * 
	 * public List<FlightOrder> getFlightsOnDay(String depart, String arrive) {
	 * return flightsOnDay.stream().filter((a) -> { Route r =
	 * a.getOutFlight().getRoute(); return r.getDepartureCityAirport() == depart &&
	 * r.getArriveCityAirport() == arrive; }).collect(Collectors.toList()); }
	 */
}
