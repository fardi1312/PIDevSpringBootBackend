package tn.esprit.pidevspringbootbackend.DAO.Entities.Amira;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Amira.InscriptionStatus;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity

public class Inscription implements Serializable {
    //id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,updatable = false)

    private Long idInscription;

    //Date d’inscription
    @Temporal(TemporalType.TIMESTAMP)
    private Date enrollmentDate;

    //status
    @Enumerated(EnumType.STRING)
    private InscriptionStatus inscriptionStatus;

    @ManyToOne
    private User user;

    //Abonnement
    @ManyToOne
    private Subscription subscription;
    @ManyToOne
    private User usera;
}
