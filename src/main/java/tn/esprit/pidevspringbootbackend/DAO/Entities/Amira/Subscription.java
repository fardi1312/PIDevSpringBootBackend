package tn.esprit.pidevspringbootbackend.DAO.Entities.Amira;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Amira.SubscriptionType;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity

public class Subscription implements Serializable {
    //id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,updatable = false)

    private Long idSubscription;

    //prix abonnement
    private double price;

    //durée
    private int durationInMonths;

    //type
    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    @OneToMany(mappedBy = "subscription")
    private List<Inscription> inscriptions;

}