package project.FPT.flightapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * <p>Generated City class
 * <p>Fields include ID, Name, Airport
 * 
 * <p>deserilize through id? @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="cityId")
 */

@Entity
@Table(name = "city")
public class City{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CityId")
	private Integer cityId;

	@Column(name = "Name")
	private String name;

	@Column(name = "Airport")
	private String airport;

	@Column(name = "VNAcode")
	private String vnaCode;
	
	// @JsonInclude()
	@OneToMany(mappedBy = "depart", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value = "departReference")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Route> departRoutes = new ArrayList<>();

	// @JsonInclude()
	@OneToMany(mappedBy = "arrive", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value = "arriveReference")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Route> arriveRoutes = new ArrayList<>();

	public City() {
	};

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAirport() {
		return airport;
	}

	public void setAirport(String airport) {
		this.airport = airport;
	}

	public List<Route> getDepartRoutes() {
		return departRoutes;
	}

	public void setDepartRoutes(List<Route> departRoutes) {
		this.departRoutes = departRoutes;
	}

	public List<Route> getArriveRoutes() {
		return arriveRoutes;
	}

	public void setArriveRoutes(List<Route> arriveRoutes) {
		this.arriveRoutes = arriveRoutes;
	}

	/**
	 * @return the vnaCode
	 */
	public String getVnaCode() {
		return vnaCode;
	}

	/**
	 * @param vnaCode the vnaCode to set
	 */
	public void setVnaCode(String vnaCode) {
		this.vnaCode = vnaCode;
	}


}
