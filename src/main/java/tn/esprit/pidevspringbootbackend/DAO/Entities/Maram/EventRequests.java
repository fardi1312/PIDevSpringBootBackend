package tn.esprit.pidevspringbootbackend.DAO.Entities.Maram;

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
@ToString
public class EventRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long EventRequestId;

    @JsonIgnore
    @ManyToOne

    Events events;
    @JsonIgnore
    @ManyToOne
    private User user ;

}
