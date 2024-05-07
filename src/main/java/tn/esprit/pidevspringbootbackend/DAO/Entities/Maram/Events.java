package tn.esprit.pidevspringbootbackend.DAO.Entities.Maram;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Maram.Category;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Maram.EventStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@ToString

public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long EventID;
    private String Title;
    private Date DateE ;
    private LocalDate DateCreation;
    private String Description;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String Image;
    private int AttendeeCount;
    private int AttendeeRest;
    private int rating;
    private float price;
    private String conditionOfParticipation;
    private boolean favoris;
    private String contactInfo;




    @Column(name = "likes_count")
    private int likesCount;



    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
    @JsonIgnore
    @ManyToOne
    User userE;

    @JsonIgnore
    @OneToMany (mappedBy = "event")
    private List<CommentMaram> comments = new ArrayList<>() ;

    @JsonIgnore
    @ManyToMany(mappedBy = "likedEvents")
    private List<User> likedByUsers = new ArrayList<>();







}
