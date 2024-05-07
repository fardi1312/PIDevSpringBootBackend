package tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.Events;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.LikePost;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Panier9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Post9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.RequestPost9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;

import java.time.LocalDateTime;
import java.util.*;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CollocationOffer> collocationOffers = new ArrayList<>();
    @JsonIgnore
    @OneToMany
    private List<CollocationRequest> collocationRequests = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private CollocationPreferences collocationPreferences;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CollocationFeedback> collocationFeedbacks = new ArrayList<>();
    @JsonIgnore
    @OneToOne
    PointCount pointCount;
    @JsonIgnore
    @OneToMany(mappedBy = "userO")
    private List<CarpoolingOffer> carpoolingOffers = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "userR")
    private List<CarpoolingRequest> carpoolingRequests = new ArrayList<>();
    @JsonIgnore
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


    /////////////////sprint 2///////////////////////

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "follow_users",
            joinColumns = @JoinColumn(name = "followed_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followerUsers = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "followerUsers")
    private List<User> followingUsers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Post> postList;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Comment> comments;



    @JsonIgnore
    @ManyToMany(mappedBy = "likeList")
    private List<Post> likedPosts = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "likeList")
    private List<Comment> likedComments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(idUser, user.idUser) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, email);
    }


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private List<Post9ach> Post9achs;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private List<RequestPost9ach> RequestPost9achs=new ArrayList<>();

@JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private List<Panier9ach> Panier9achs=new ArrayList<>();

    @JsonIgnore
    @OneToOne
    LikePost likePost;

    @OneToMany(mappedBy = "userE")
    private List<Events> eventss;

    @ManyToMany
    @JoinTable(
            name = "user_likes_event",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Events> likedEvents = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "president")

    private Club club ;
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<ClubMembership> clubMembership ;

}
