package tn.esprit.pidevspringbootbackend.DAO.Response;

import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Post;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Post post;
    private Boolean likedByAuthUser;
}
