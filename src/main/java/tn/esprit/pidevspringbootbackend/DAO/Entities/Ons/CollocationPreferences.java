package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Gender;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Interest;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Pets;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.RoomType;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollocationPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCollocationPreferences ;
    @Enumerated(EnumType.STRING)

    private Pets pets;
    private Boolean smoking ;
    private float budget ;
    @Enumerated(EnumType.STRING)

    private Gender gender ;
    @Enumerated(EnumType.STRING)

    private Interest interest;
    @Enumerated(EnumType.STRING)

    private RoomType roomType ;
    private int HouseType ;
    private String location ;
    @ManyToOne
    private User user ;


}
