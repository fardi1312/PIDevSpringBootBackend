package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


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
    private String name ;
    private String description ;
    @OneToOne
    private User president;

    @OneToMany(mappedBy = "club")
    private List<User> members = new ArrayList<>();




}
