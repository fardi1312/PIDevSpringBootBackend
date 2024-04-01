package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollocationPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCollocationPreferences;
    @Enumerated(EnumType.STRING)
    private Pets pets;
    private Boolean smoking;
    private float budget;
    @Enumerated(EnumType.STRING)

    private Gender gender;
    @Enumerated(EnumType.STRING)
    @Column(length = 20) // Set the length to a value that can accommodate all enum values

    private Interest interest;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private int HouseType;
    private String location;
    @Enumerated(EnumType.STRING)
    private FurnitureCollocation furnitureCollocation ;
    @OneToOne
    @JsonIgnore
    private User user;
}


