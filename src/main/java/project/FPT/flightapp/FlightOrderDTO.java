package project.FPT.flightapp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FlightOrderDTO {
	private Integer orderId;
	private String customer;
	private double price;
	
	@JsonIgnore
	private FlightDetail outFlight;
	@JsonIgnore
	private FlightDetail returnFlight;
	@JsonIgnore
	private Day dayOfFlight;
	@JsonIgnore
	private Ticket ticket;
	
	public FlightOrderDTO() {
	}
	
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
	 * @return the ticket
	 */
	public Ticket getTicket() {
		return ticket;
	}
	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	
	/**
	 * @return the outflight Id, can be null if dto
	 */
	public Integer getOutFlightId() {
		return this.getOutFlight() == null ? null : outFlight.getFlightId();
	}
	
	/**
	 * @return the outflight Time, can be null if dto
	 */
	public LocalDateTime getOutFlightTime() {
		return this.getOutFlight() == null ? null : outFlight.getDeparture();
	}
	
	/**
	 * @return the arrive Time, can be null if dto
	 */
	public LocalDateTime getArriveFlightTime() {
		return this.getOutFlight() == null ? null : outFlight.getArrival();
	}

	/**
	 * @return the return Id 
	 * <p>Integer value, null if no return flight
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

	public LocalDate getDate() {
		return this.dayOfFlight.getDay();
	}
	
	public int getMonth() {
		return this.dayOfFlight.getMonthValue();
	}
	
	public Integer getMonthId() {
		return this.dayOfFlight.getMonth().getMonthId();
	}

	public String getDayOfWeek() {
		return DayOfWeek.from(this.dayOfFlight.getDay()).name();
	}
	
	public String getOutDepartCity() {
		return this.getOutFlight() == null ? null : this.outFlight.getRoute().getDepart().getName();
	}
	
	public String getOutArriveCity() {
		return this.getOutFlight() == null ? null : this.outFlight.getRoute().getArrive().getName();
	}
	
	public String getRouteName() {
		return this.getOutFlight() == null ? null : this.outFlight.getRoute().getName();
	}
	
	public Integer getTicketId() {
		return this.getTicket() == null ? null : this.ticket.getTicketId();
	}
	
	public String getTicketType() {
		return this.getTicket() == null ? null : this.ticket.getType();
	}
	
	public String getTicketSeat() {
		return this.getTicket() == null ? null : this.ticket.getSeat();
	}
}
