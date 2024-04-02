package tn.esprit.pidevspringbootbackend.DAO.Entities.Amira;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Amira.DurationType;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Amira.SubscriptionType;


import java.util.Date;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    @Enumerated(EnumType.STRING)
    private SubscriptionType type;
    @Enumerated(EnumType.STRING)
    private DurationType duration;




}

