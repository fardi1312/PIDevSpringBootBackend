package tn.esprit.pidevspringbootbackend.DAO.Entities.SM;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Gender;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingStatus;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class CarpoolingOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long CarpoolingOfferID;
    private String Location;
    @ElementCollection
    private List<Date> DateAller = new ArrayList<>();
    @ElementCollection
    private List<Date> DateRetour = new ArrayList<>();
    private int PlaceDispoAller;
    private int PlaceDispoRetour;
    private double Price;
    private String Description;
    private String Img;
    @Enumerated(EnumType.STRING)
    private CarpoolingStatus carpoolingStatus;
    private Date OfferDate;
    @Enumerated(EnumType.STRING)
    private Gender ForWho;
    @Enumerated(EnumType.STRING)
    private CarpoolingType carpoolingType;

    private String radioon;
    private Boolean climatise ;
    private Boolean chauffage ;
    private Boolean smoking ;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="carpoolingoffer")

    private List<CarpoolingRequest> CarpoolingRequests  = new ArrayList<>();
    @JsonIgnore
    @ManyToOne
    User userO;


}
