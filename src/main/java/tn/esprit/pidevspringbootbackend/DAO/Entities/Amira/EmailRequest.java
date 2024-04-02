package tn.esprit.pidevspringbootbackend.DAO.Entities.Amira;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "email_request") // Specify the table name explicitly to avoid conflicts

public class EmailRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(name = "to_address") // Rename the column to "toAddress" or any other suitable name
    private String toAddress;
    private String subject;
    private String body;

}