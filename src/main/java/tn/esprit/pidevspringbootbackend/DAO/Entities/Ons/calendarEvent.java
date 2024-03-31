package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @Column(length = 1024) // Change 1024 to your desired length

    private byte[] qrCodeOfferer;
    @Column(length = 1024) // Change 1024 to your desired length

    private byte[] qrCodeRequester;


    @Temporal(TemporalType.TIMESTAMP)
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    private Date end;
    @JsonIgnore
    @ManyToMany
    private List<User> users = new ArrayList<>();


}
