package tn.esprit.pidevspringbootbackend.DAO.Entities.SM;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Coordinates {
    private double latitude;
    private double longitude;
}