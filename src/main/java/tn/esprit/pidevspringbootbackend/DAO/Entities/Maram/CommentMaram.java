package tn.esprit.pidevspringbootbackend.DAO.Entities.Maram;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@ToString
public class CommentMaram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id") // Specify the foreign key column
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id") // Assuming there's an Events entity
    private Events event;
}