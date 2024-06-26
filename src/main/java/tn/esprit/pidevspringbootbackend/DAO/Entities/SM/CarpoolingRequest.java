package tn.esprit.pidevspringbootbackend.DAO.Entities.SM;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;
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
public class CarpoolingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long CarppoolingRequestId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")

    private Date ReservationDate;
    private int NbPlacesAller;
    private int NbPlacesRetour;
    @Enumerated(EnumType.STRING)
    private Request requestStatus;
    private double priceRequeste;
    @Enumerated(EnumType.STRING)
    private CarpoolingType requestType;
private String location;
    @ElementCollection
    private List<Date> DateRetourReserver = new ArrayList<>();
    @ElementCollection
    private List<Date> DateAllerReserver = new ArrayList<>();

    @JsonIgnore
    @ManyToOne

    CarpoolingOffer carpoolingoffer;
    @JsonIgnore
    @ManyToOne
    private User userR ;



}
