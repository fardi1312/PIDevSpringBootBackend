package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String email ;
    private boolean emailVerified;
    private int refusedCount = 0 ;
    private Date date = new Date() ;

    private String position;
    private String description;
    @ManyToOne
    @JsonIgnore
    private Club club;

    @ManyToOne
    private User member ;

}
