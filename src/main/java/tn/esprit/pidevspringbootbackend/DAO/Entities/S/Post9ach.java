package tn.esprit.pidevspringbootbackend.DAO.Entities.S;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingStatus;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post9ach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdPost9ach;
    private String title;
    private String description;
    private String image;
    private double price;
    private String quantity;
    private String location;
    private Long nbslikes;
    private Long quantitySold;



    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date OfferDate;

    private String Category ;
    private String etat ;
    @Enumerated(EnumType.STRING)
    private CarpoolingStatus statutPost;

@JsonIgnore
    @ManyToOne
    User user;


    @JsonIgnore
    @ManyToOne
    Panier9ach panier9ach;
    @JsonIgnore
    @ManyToMany(mappedBy="post9aches", cascade = CascadeType.ALL)
    private List<LikePost> likeposts;

}
