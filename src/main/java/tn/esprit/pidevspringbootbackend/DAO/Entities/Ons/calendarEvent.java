package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class calendarEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private  String Requester;
    private  String Offerer ;
    boolean draggable = true;
    private long idOfferer;
    private long idRequester;
    private long idCollocationRequest;
    private Boolean fixedOfferer=null;
    private Boolean fixedRequester= null;
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;
    private long idClub ;
    private String meetingLink ;

    @Temporal(TemporalType.TIMESTAMP)
    private Date end;
    private long collocationOfferId ;
    private boolean type ;
    private long memberShipApplicationId ; 

    @ManyToMany
    private List<User> users = new ArrayList<>();
    @Embedded
    private Resizable resizable ;
    private Boolean acceptRenting ;
    private Boolean acceptRenter ;


}

