package project.FPT.flightapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import project.FPT.flightapp.Month;

public interface MonthRepository extends JpaRepository<Month, Integer> {
	public Month findByValue(int val);

	@Query(value = "SELECT * FROM month where Value = :val AND Year = :year", nativeQuery = true)
	public Month findByBoth(@Param("val") int val, @Param("year") int year);
}
