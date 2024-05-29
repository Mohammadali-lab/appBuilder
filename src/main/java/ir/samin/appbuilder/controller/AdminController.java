package ir.samin.appbuilder.controller;


import ir.samin.appbuilder.dao.TicketRepository;
import ir.samin.appbuilder.dto.AdminCreationDTO;
import ir.samin.appbuilder.dto.TicketAnswerRequestDTO;
import ir.samin.appbuilder.entity.Ticket;
import ir.samin.appbuilder.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.badRequest;

public class AdminController {

    private AdminService adminService;

    private TicketRepository ticketRepository;

    public AdminController(AdminService adminService, TicketRepository ticketRepository) {

        this.adminService = adminService;
        this.ticketRepository = ticketRepository;
    }

    @PreAuthorize("hasAuthority('CREATE_ADMINS')")
    @PostMapping("/create")
    public ResponseEntity addAdmin(@Valid @RequestBody AdminCreationDTO adminCreationDTO) {
        UserDetails userDetails = adminService.loadUserByUsername(adminCreationDTO.getMobileNumber());
        if (userDetails != null) {
//            adminService.addLoginHistory(adminCreationDTO, ip, false);
            return badRequest()
                    .body(new ApiError(null, "username exist.", "username exist."));
        } else {
            adminService.createAdminFullDTO(adminCreationDTO);

            return ResponseEntity.ok("Admin Created Successfully");
        }
    }


}
