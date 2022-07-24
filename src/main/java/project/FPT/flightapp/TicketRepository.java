package project.FPT.flightapp;

import org.springframework.data.jpa.repository.JpaRepository;
import project.FPT.flightapp.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer>{
	
}
