package project.FPT.flightapp;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "Ticket")
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TicketId")
	private Integer ticketId;
	
	@Column(name = "FlightType")
	private String type;
	
	@Column(name = "SeatType")
	private String seat;
	
	/*
	 * @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval =
	 * true)
	 * 
	 * @JsonManagedReference(value = "ticketReference")
	 * 
	 * @JsonIgnore
	 * 
	 * @Fetch(value = FetchMode.SUBSELECT) private List<FlightOrder> FlightsOnTicket
	 * = new ArrayList<>();
	 */

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	/**
	 * @return the flightsOnTicket
	 */
	/*
	 * public List<FlightOrder> getFlightsOnTicket() { return FlightsOnTicket; }
	 */

}
