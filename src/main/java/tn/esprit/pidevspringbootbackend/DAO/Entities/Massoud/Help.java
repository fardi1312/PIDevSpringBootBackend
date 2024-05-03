package tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Helps")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Help {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;


    public Help(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
