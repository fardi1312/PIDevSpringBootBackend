package tn.esprit.pidevspringbootbackend.DAO.Entities.SM;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Gender;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Interest;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Pets;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.RoomType;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingType;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class CarpoolingPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private long idCarpoolingPreferences ;

    private Float budget ;
    @Enumerated(EnumType.STRING)
    private Gender gender ;
    @Enumerated(EnumType.STRING)
    private CarpoolingType carpoolingType ;
    private Integer nbPlaces ;
    private String location ;
    private String Description;
    private Boolean radioon;
    private Boolean climatise ;
    private Boolean chauffage ;
    private Boolean smoking ;
    @JsonIgnore
    @OneToOne
    private User userS ;


}