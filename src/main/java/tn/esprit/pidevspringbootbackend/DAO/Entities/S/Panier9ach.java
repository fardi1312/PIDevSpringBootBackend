package tn.esprit.pidevspringbootbackend.DAO.Entities.S;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.PanierStatut;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Panier9ach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdPanier9ach;
    @Enumerated(EnumType.STRING)
 PanierStatut satatuspanier;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date BuyDate;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="panier9ach")
    private List<Post9ach> Post9achs   = new ArrayList<>();
    double totaleprice;


    @JsonIgnore
    @OneToOne(mappedBy="panier9ach")
    private RequestPost9ach requestpost9ach;


    @ManyToOne
    User user;
}
