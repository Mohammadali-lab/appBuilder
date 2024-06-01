package ir.samin.appbuilder.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
public class User extends BaseEntity {


    @Column(name = "username")
    private String username;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "confirm_code")
    private String confirmCode;
    @Column(name = "confirm_code_register_time")
    private LocalDateTime confirmCodeRegisterTime;
    @Column(name = "is_enabled")
    private boolean isEnabled;
    @Column(name = "is_confirmed")
    private boolean isConfirmed;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Ticket> tickets;

    public void add(Ticket ticket) {

        if(ticket != null) {
            if(tickets == null) {
                tickets = new HashSet<>();
            }

            tickets.add(ticket);
            ticket.setUser(this);
        }
    }
}
