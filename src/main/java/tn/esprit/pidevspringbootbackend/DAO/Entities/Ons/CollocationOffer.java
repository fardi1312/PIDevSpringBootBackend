package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.FurnitureCollocation;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Gender;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollocationOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long IdCollocationOffer ;
    private String location ;
    private int houseType ;
    @OneToMany(mappedBy ="collocationOffer")
    private List<RoomDetails> roomDetailsList;
    private int availablePlaces ;
    private Date dateRent ;
    private Date dateOffer ;
    private Gender gender ;
    @ElementCollection
    private List<String> imageCollocation = new ArrayList<>();
    private float price ;
    @Enumerated(EnumType.STRING)
    private FurnitureCollocation furnitureCollocation ;
    @Lob
    private String descriptionCollocation ;
  //  private int collocationRating ;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "collocationOffer")
    private  List<CollocationRequest> collocationRequests = new ArrayList<>() ;


}
