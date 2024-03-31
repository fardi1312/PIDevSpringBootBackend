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
    private String    locationLx;
    private String       locationLy;



    private String governorate;
    private String country;
    private String city;
    private String streetAddress;

    private int houseType ;
    @OneToMany(mappedBy ="collocationOffer",cascade = CascadeType.ALL)
    @JsonManagedReference

    private List<RoomDetails> roomDetailsList;
    private int availablePlaces ;
    private Date dateRent ;
    private Date dateOffer ;
    private Gender gender ;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String imageCollocation ;
    private float price ;
    @Enumerated(EnumType.STRING)
    private FurnitureCollocation furnitureCollocation ;
    @Lob
    private String descriptionCollocation ;
    private Boolean smokingAllowed;
    @Enumerated(EnumType.STRING)
    private Pets petsAllowed ;
    @Enumerated(EnumType.STRING)
    private Interest interest;

    @ManyToOne
    @JsonIgnore

    private User user;

    @OneToMany(mappedBy = "collocationOffer")
    @JsonIgnore

    private  List<CollocationRequest> collocationRequests = new ArrayList<>() ;
    @OneToMany(mappedBy = "collocationOffer")
    @JsonIgnore

    private  List<CollocationFeedback> collocationFeedbacks = new ArrayList<>();

    private int matchPercentage;

}
