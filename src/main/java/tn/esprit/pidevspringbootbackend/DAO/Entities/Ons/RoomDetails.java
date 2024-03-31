package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.RoomType;

import java.util.ArrayList;
import java.util.List;

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
    private boolean selected ;
    private int requestedPlaces;

    @ManyToOne
    @JsonBackReference
    private CollocationOffer collocationOffer;
}
