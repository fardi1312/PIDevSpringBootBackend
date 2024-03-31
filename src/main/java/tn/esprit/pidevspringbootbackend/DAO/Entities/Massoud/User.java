package tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationFeedback;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

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

    @Column(length = 1000)
    @Lob
    private String profilePhoto;


    @Column(length = 256)
    private String coverPhoto;

    private Integer followerCount;
    private Integer followingCount;

    @Column
    private Boolean enabled;

    @Column
    private Boolean accountVerified = false;

    @Column(nullable = false)
    private Boolean emailVerified;

    @Column(length = 20)
    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateLastModified;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<CollocationOffer> collocationOffers = new ArrayList<>();

    @OneToMany
    private List<CollocationRequest> collocationRequests = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private CollocationPreferences collocationPreferences;

    @OneToMany(mappedBy = "user")
    private List<CollocationFeedback> collocationFeedbacks = new ArrayList<>();



    @OneToOne
    PointCount pointCount;

    @OneToMany(mappedBy = "userO")
    private List<CarpoolingOffer> carpoolingOffers = new ArrayList<>();

    @OneToMany(mappedBy = "userR")
    private List<CarpoolingRequest> carpoolingRequests = new ArrayList<>();

    @OneToOne
    CarpoolingPreferences carpoolingPreferences;



    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}