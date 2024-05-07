package tn.esprit.pidevspringbootbackend.DAO.Entities.S;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LikePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdPost9ach;
private  Long etat;
    @JsonIgnore
    @OneToOne
    User user;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Post9ach> post9aches;
}
