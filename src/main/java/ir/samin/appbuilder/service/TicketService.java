package ir.samin.appbuilder.service;

import ir.samin.appbuilder.dao.TicketRepository;
import ir.samin.appbuilder.dao.UserRepository;
import ir.samin.appbuilder.entity.Admin;
import ir.samin.appbuilder.entity.Ticket;
import ir.samin.appbuilder.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private TicketRepository ticketRepository;
    private UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }


    public void setAnswer(Admin admin, Ticket ticket, String answer) {

        Ticket answerTicket = new Ticket();
        answerTicket.setMessage(answer);
        answerTicket.setAdmin(admin);
        answerTicket.setAnswer(ticket);
        ticketRepository.save(answerTicket);
        ticket.setAnswer(answerTicket);
        ticketRepository.save(ticket);
    }

    public Set<Ticket> getUserUnansweredTickets(String phoneNumber) {

        User user = userRepository.findByUsername(phoneNumber).get();

        Set<Ticket> tickets = user.getTickets();

        tickets = tickets.stream().filter(ticket -> ticket.getAdmin()==null).collect(Collectors.toSet());
        return tickets;
    }

    public Set<Ticket> getUserAnsweredTickets(String phoneNumber) {

        User user = userRepository.findByUsername(phoneNumber).get();

        Set<Ticket> tickets = user.getTickets();

        tickets = tickets.stream().filter(ticket -> ticket.getAdmin()!=null).collect(Collectors.toSet());
        return tickets;
    }

}
