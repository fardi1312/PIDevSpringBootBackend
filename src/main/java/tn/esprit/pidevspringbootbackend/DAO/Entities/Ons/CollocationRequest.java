package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


import jakarta.persistence.*;
import lombok.*;

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

    private tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request Request ;
    private int houseType ;
    private int places ;
    @Lob
    private String feedback ;
    @OneToMany
    private List<RoomDetails> RoomDetails;
    private String Description ;
    @ManyToOne
    private CollocationOffer collocationOffer;

}
