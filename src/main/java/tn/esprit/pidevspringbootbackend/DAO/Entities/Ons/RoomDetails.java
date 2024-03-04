package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.RoomType;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRoomDetails;
    private int availablePlaces;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private float prix ;

    @ManyToOne
    @JsonBackReference
    private CollocationOffer collocationOffer;
    @ManyToOne
    private CollocationRequest collocationRequest ;
}
