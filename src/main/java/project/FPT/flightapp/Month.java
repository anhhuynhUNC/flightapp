package project.FPT.flightapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "Month")
public class Month {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MonthId")
	private Integer monthId;

	@Column(name = "Name")
	private String name;

	@Column(name = "Value")
	private int value;
	
	@Column(name = "Year")
	private int year;
	
	@OneToMany(mappedBy = "month", cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonManagedReference(value = "monthReference")
	private List<Day> days = new ArrayList<>();

	public Integer getMonthId() {
		return monthId;
	}
	
	public List<Day> getDays() {
		return days;
	}
	
	public LocalDate getDayFromMonth(int i) {
		return this.getDays().get(i-1).getDay();
	}

	public void setMonthId(Integer monthId) {
		this.monthId = monthId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	

}
