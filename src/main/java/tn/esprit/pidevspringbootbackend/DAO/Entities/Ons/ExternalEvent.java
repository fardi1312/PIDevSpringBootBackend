package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ExternalEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    private Date end;

    private Date requestStart ;
    private Date requestEnd ;

    private boolean draggable;
    @OneToOne
    CollocationRequest collocationRequest ;
    @OneToOne
    CollocationOffer collocationOffer ;
    @ManyToOne
    User user ;


}
