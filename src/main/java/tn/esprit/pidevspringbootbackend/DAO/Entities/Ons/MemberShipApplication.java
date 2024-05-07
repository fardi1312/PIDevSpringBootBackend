package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberShipApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated
    private Request status ;
    private String interviewer ;
    private String position ;
    private Date date = new Date();
    @ManyToOne
    private User user ;
    @ManyToOne
    @JsonIgnore
    private Club club ;
    private String message;
}
