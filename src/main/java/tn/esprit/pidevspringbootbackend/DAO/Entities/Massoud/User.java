package tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Inscription;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationFeedback;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(length = 128, nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 256, nullable = false)
    @JsonIgnore
    private String password; // Use a secure password hashing mechanism!

    @Column(length = 64, nullable = false)
    private String firstName;

    @Column(length = 64, nullable = false)
    private String lastName;

    @Column(length = 100)
    private String intro;

    @Column(length = 16)
    private String gender;

    @Column(length = 128)
    private String hometown;

    @Column(length = 128)
    private String currentCity;

    @Column(length = 128)
    private String eduInstitution;

    @Column(length = 128)
    private String workplace;

    @Column(length = 256)
    private String profilePhoto;

    @Column(length = 256)
    private String coverPhoto;



    private Integer followerCount;
    private Integer followingCount;

    @Column(nullable = false)
    private Boolean enabled; // Account enabled status

    @Column(nullable = false)
    private Boolean accountVerified; // Account verification status

    @Column(nullable = false)
    private Boolean emailVerified; // Email verification status

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate; // Store as LocalDate

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate; // Store as LocalDateTime

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateLastModified; // Store as LocalDateTime


    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToMany (mappedBy = "user")
    private List<CollocationOffer> collocationOffers = new ArrayList<>() ;
    @OneToMany
    private  List<CollocationRequest> collocationRequests = new ArrayList<>() ;
    @OneToMany(mappedBy = "user")
    private List<CollocationPreferences> collocationPreferences = new ArrayList<>();
    @ManyToMany (mappedBy = "users")
    private List<CollocationFeedback> collocationFeedbacks = new ArrayList<>();



    @OneToOne
    PointCount pointCount ;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CarpoolingOffer> CarpoolingOffers  = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<CarpoolingRequest> CarpoolingRequests = new ArrayList<>();
    @OneToMany(mappedBy = "userS")
    private List<CarpoolingPreferences> carpoolingPreferences = new ArrayList<>();
    @OneToMany(mappedBy = "usera")
    private List<Inscription> inscriptions;
}
