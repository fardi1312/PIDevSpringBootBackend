package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CollocationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCollocationRequest ;
    @Enumerated(EnumType.STRING)
    private Request request ;
    private int houseType ;
    private int places ;
    @Lob
    private String feedback ;
    @OneToMany
    private List<RoomDetails> RoomDetails;
    private String Description ;
    @JsonIgnore
    @ManyToOne
    private CollocationOffer collocationOffer;

}
