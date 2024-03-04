package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @OneToMany(mappedBy ="collocationOffer",cascade = CascadeType.ALL)
    @JsonManagedReference

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
  //
  //  private int collocationRating ;
    @ManyToOne
    @JsonIgnore

    private User user;

    @OneToMany(mappedBy = "collocationOffer")
    @JsonIgnore

    private  List<CollocationRequest> collocationRequests = new ArrayList<>() ;
    @OneToMany(mappedBy = "collocationOffer")
    @JsonIgnore

    private  List<CollocationFeedback> collocationFeedbacks = new ArrayList<>();


}
