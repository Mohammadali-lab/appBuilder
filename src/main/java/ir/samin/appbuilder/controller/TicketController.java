package ir.samin.appbuilder.controller;

import ir.samin.appbuilder.dao.AdminRepository;
import ir.samin.appbuilder.dao.TicketRepository;
import ir.samin.appbuilder.dao.UserRepository;
import ir.samin.appbuilder.dto.TicketAnswerRequestDTO;
import ir.samin.appbuilder.entity.Admin;
import ir.samin.appbuilder.entity.Ticket;
import ir.samin.appbuilder.entity.User;
import ir.samin.appbuilder.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Security;
import java.util.Optional;
import java.util.Set;

@RestController
public class TicketController {

    private TicketService ticketService;

    private AdminRepository adminRepository;

    private UserRepository userRepository;

    private TicketRepository ticketRepository;

    public TicketController(TicketService ticketService, TicketRepository ticketRepository,
                            AdminRepository adminRepository, UserRepository userRepository){
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }


    @PreAuthorize("hasAnyAuthority('SEE_TICKETS')")
    @GetMapping("/get-unanswered-tickets")
    public ResponseEntity getUnansweredTickets(){

        return ResponseEntity.ok(ticketRepository.findAllByAnswerIsNull());
    }

    @GetMapping("/user-unanswered-tickets")
    public ResponseEntity getUserUnansweredTickets(){

        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();

        Set<Ticket> tickets = ticketService.getUserUnansweredTickets(phoneNumber);
        return ResponseEntity.ok(tickets);
    }

    public ResponseEntity getUserAnsweredTickets() {

        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();

        Set<Ticket> tickets = ticketService.getUserAnsweredTickets(phoneNumber);
        return ResponseEntity.ok(tickets);
    }


    @PreAuthorize("hasAnyAuthority('SET_ANSWER_TICKET')")
    @PostMapping("/answer-ticket")
    public ResponseEntity answerTheTicket(@RequestBody TicketAnswerRequestDTO requestDTO){

        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();

        Admin admin = adminRepository.findByUsername(phoneNumber).get();
        Ticket ticket = ticketRepository.findById(requestDTO.getTicketId()).get();
        ticketService.setAnswer(admin, ticket, requestDTO.getAnswer());
        return ResponseEntity.ok("answer saved successfully");

    }
}
