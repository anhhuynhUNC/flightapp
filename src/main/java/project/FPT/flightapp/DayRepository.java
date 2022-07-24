package project.FPT.flightapp;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import project.FPT.flightapp.Day;

public interface DayRepository extends JpaRepository<Day,Integer>{
	Day findByDay(LocalDate date);
}
