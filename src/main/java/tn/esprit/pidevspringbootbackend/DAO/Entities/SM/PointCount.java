package tn.esprit.pidevspringbootbackend.DAO.Entities.SM;

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
public class PointCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long IdPointCount;
   private double nbPoint;

}
