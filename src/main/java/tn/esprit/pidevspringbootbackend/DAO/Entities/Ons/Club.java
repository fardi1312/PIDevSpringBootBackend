package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    @Enumerated
    private Category category ;
    @Column(columnDefinition = "MEDIUMBLOB")
    private String logo ;
    private String name ;
    private String description ;
    private String facebookUrl;
    private String twitterUrl;
    private String instagramUrl;
    private String linkedinUrl;
    private String otherCategory ;
    private Boolean openMembership ;
    private Boolean recruiting ;
    @OneToMany
    private List<MemberShipApplication> memberShipApplications = new ArrayList<>() ;

    @OneToOne
    private User president;
    @OneToMany(mappedBy = "club",cascade = CascadeType.MERGE)
    private List<ClubMembership> clubMemberShip = new ArrayList<>() ;





}
