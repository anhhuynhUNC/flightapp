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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "airline")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Airline {
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "AirlineId")
		private Integer airlineId;
		
		@Column(name = "Name")
		private String name;

		@Column(name = "Code")
		private String code;
		
		@JsonIgnore
		@OneToMany(mappedBy = "airline", cascade = CascadeType.ALL, orphanRemoval = true)
		@JsonManagedReference(value = "airlineReference")
		@Fetch(value = FetchMode.SUBSELECT)
		private List<FlightDetail> flights = new ArrayList<>();
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Integer getAirlineId() {
			return airlineId;
		}

		public void setAirlineId(Integer airlineId) {
			this.airlineId = airlineId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		
	
}
