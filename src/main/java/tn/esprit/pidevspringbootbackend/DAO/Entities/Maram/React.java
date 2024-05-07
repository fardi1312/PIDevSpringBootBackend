package tn.esprit.pidevspringbootbackend.DAO.Entities.Maram;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Maram.ReactionType;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class React {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId; // ID of the event this reaction belongs to
    private Long userId; // ID of the user who reacted
    private ReactionType reaction; // Enum representing the reaction (like/dislike)

}
