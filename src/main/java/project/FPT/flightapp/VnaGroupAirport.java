package project.FPT.flightapp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VnaGroupAirport {
	@JsonProperty("Code")
	private String code;
	@JsonProperty("DLGroups")
	private List<VnaGroup> dlGroups;

	public VnaGroupAirport() {
		dlGroups = new ArrayList<>();
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the dLGroups
	 */
	public List<VnaGroup> getDlGroups() {
		return dlGroups;
	}

	/**
	 * @param dLGroups the dLGroups to set
	 */
	public void setDlGroups(List<VnaGroup> dlGroups) {
		this.dlGroups = dlGroups;
	}
	
}
