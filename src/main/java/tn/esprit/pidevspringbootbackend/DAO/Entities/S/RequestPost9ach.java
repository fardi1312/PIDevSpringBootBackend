package tn.esprit.pidevspringbootbackend.DAO.Entities.S;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class RequestPost9ach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdRequestPost9ach;
    @JsonIgnore
    @OneToOne
    private Panier9ach panier9ach;
    @JsonIgnore
    @ManyToOne
    User user;


}
