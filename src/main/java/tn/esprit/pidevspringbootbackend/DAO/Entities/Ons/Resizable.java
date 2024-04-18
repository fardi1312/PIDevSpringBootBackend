package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
public class Resizable {
    private boolean beforeStart = true;
    private boolean afterEnd = true;

    public Resizable() {
        this.beforeStart = true;
        this.afterEnd = true;
    }


}
