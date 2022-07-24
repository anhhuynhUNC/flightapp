package project.FPT.flightapp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VnaGroup {
	@JsonProperty("Airports")
	private List<VnaGroupAirport> airports;
	@JsonProperty("DisplayName")
	private String displayName;
	@JsonProperty("Id")
	private String id;
	
	public VnaGroup() {
		airports = new ArrayList<>();
	}

	/**
	 * @return the airports
	 */
	public List<VnaGroupAirport> getAirports() {
		return airports;
	}

	/**
	 * @param airports the airports to set
	 */
	public void setAirports(List<VnaGroupAirport> airports) {
		this.airports = airports;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
}
