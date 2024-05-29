package ir.samin.appbuilder.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "admin")
@Setter
@NoArgsConstructor
@Getter
public class Admin extends BaseEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "admin")
    private Set<Ticket> tickets;
}
