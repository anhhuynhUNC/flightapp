package project.FPT.flightapp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VnaCollection{
	@JsonProperty("Airport")
	private List<VnaAirports> airport;
	@JsonProperty("Groups")
	private List<VnaGroup> group;
	
	public VnaCollection() {
		airport = new ArrayList<>();
		group = new ArrayList<>();
	}

	/**
	 * @return the airport
	 */
	public List<VnaAirports> getAirport() {
		return airport;
	}

	/**
	 * @param airport the airport to set
	 */
	public void setAirport(List<VnaAirports> airport) {
		this.airport = airport;
	}

	/**
	 * @return the group
	 */
	public List<VnaGroup> getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(List<VnaGroup> group) {
		this.group = group;
	};
	
	

}
