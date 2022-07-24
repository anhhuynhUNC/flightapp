package project.FPT.flightapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//commandlinerunner on start up to use with services
@Component
public class FlightAppRunner implements CommandLineRunner {
	@Autowired
	FlightService flisev;
	private static final Logger log = LoggerFactory.getLogger(FlightAppApplication.class);

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		try {
			LocalDate now = LocalDate.now();
			log.info(flisev.getAllTicketTypes().iterator().next().getType());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}