package ir.samin.appbuilder.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "admin")
@Setter
@NoArgsConstructor
@Getter
public class Ticket extends BaseEntity{

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToOne
    private Ticket answer;
}
