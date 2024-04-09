package tn.esprit.pidevspringbootbackend.DAO.Response;

import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Comment;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Comment comment;
    private Boolean likedByAuthUser;
}
