package tn.esprit.pidevspringbootbackend.DAO.Entities.SM;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.el.parser.BooleanNode;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Gender;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingStatus;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingType;

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
public class CarpoolingOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long CarpoolingOfferID;
    @Column(name = "location") // Assurez-vous que le nom de la colonne est correct
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")

    private Date DateAller ;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")

    private Date DateRetour ;
    private int PlaceDispoAller;
    private int PlaceDispoRetour;
    private double price;
    private String Description;
    private String Img;
    @Enumerated(EnumType.STRING)
    private CarpoolingStatus carpoolingStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date OfferDate;
    @Enumerated(EnumType.STRING)
    private Gender ForWho;
    @Enumerated(EnumType.STRING)
    private CarpoolingType carpoolingType;
    private double priceApres;

        private Boolean radioon;
    private Boolean climatise ;
    private Boolean chauffage ;
    private Boolean smoking ;
    private String locationLx;
    private String locationLy;
    private String traget;
    @ElementCollection
    private List<Coordinates> targets = new ArrayList<>();



    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="carpoolingoffer")

    private List<CarpoolingRequest> CarpoolingRequests  = new ArrayList<>();
@JsonIgnore
    @ManyToOne
    User userO;







}
