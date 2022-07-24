package project.FPT.flightapp;

import java.io.Serializable;
import java.time.LocalDateTime;
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

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "flightorder")
public class FlightOrder implements Serializable {

	/*
	 * @Id
	 * 
	 * @GeneratedValue(generator = "generator")
	 * 
	 * @GenericGenerator(name = "generator", strategy = "increment")
	 * 
	 * @Column(name = "OrderId")
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "OrderId")
	private Integer orderId;

	@Column(name = "Customer")
	private String customer;

	@Column(name = "Price")
	private double price;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "OutFlight")
	@JsonBackReference(value = "outflightReference")
	private FlightDetail outFlight;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ReturnFlight")
	@JsonBackReference(value = "returnflightReference")
	private FlightDetail returnFlight;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DateId")
	@JsonBackReference(value = "dayReference")
	private Day dayOfFlight;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TicketId")
	@JsonBackReference(value = "ticketReference")
	private Ticket ticket;

	/**
	 * @return the orderId
	 */
	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the outFlight
	 */
	public FlightDetail getOutFlight() {
		return outFlight;
	}

	/**
	 * @param outFlight the outFlight to set
	 */
	public void setOutFlight(FlightDetail outFlight) {
		this.outFlight = outFlight;
	}

	/**
	 * @return the outflight Id, cannot be null
	 */
	public Integer getOutFlightId() {
		return this.getOutFlight().getFlightId();
	}

	/**
	 * @return the outflight Time, cannot be null
	 */
	public LocalDateTime getOutFlightTime() {
		return this.getOutFlight().getDeparture();
	}

	/**
	 * @return the returnFlight
	 */
	public FlightDetail getReturnFlight() {
		return returnFlight;
	}

	/**
	 * @param returnFlight the returnFlight to set
	 */
	public void setReturnFlight(FlightDetail returnFlight) {
		this.returnFlight = returnFlight;
	}

	/**
	 * @return the return Id
	 *         <p>
	 *         Integer value, null if no return flight
	 */
	public Integer getReturnFlightId() {
		return this.getReturnFlight() == null ? null : returnFlight.getFlightId();
	}

	/**
	 * @return the returnflight Time
	 */
	public LocalDateTime getReturnFlightTime() {
		return this.getReturnFlight() == null ? null : returnFlight.getDeparture();
	}

	/**
	 * @return the dayOfFlight
	 */
	public Day getDayOfFlight() {
		return dayOfFlight;
	}

	/**
	 * @return the date Id
	 */
	public Integer getDayOfFlightId() {
		return dayOfFlight.getDateId();
	}

	/**
	 * @param dayOfFlight the dayOfFlight to set
	 */
	public void setDayOfFlight(Day dayOfFlight) {
		this.dayOfFlight = dayOfFlight;
	}

	/**
	 * @return the ticket
	 */
	public Ticket getTicket() {
		return ticket;
	}

	/**
	 * @return the ticket Id
	 */
	public Integer getTicketId() {
		return ticket.getTicketId();
	}

	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

}
