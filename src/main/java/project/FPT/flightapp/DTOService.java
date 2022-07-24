package project.FPT.flightapp;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DTOService {
	@Autowired
	private ModelMapper modelMapper;
	
	public FlightOrderDTO convertToDTO(FlightOrder order) {
		FlightOrderDTO orderDTO = modelMapper.map(order, FlightOrderDTO.class);
		//orderDTO.setTicket(convertToDTO(order.getTicket()));	
		return orderDTO;
	}
	
	public TicketDTO convertToDTO(Ticket ticket) {
		TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);
		//ticketDTO.setFlightsOnTicket(ticket.getFlightsOnTicket().stream().map(this::convertToDTO).collect(Collectors.toList()));
		return ticketDTO;
	}
}
