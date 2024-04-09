package tn.esprit.pidevspringbootbackend.DAO.Response;

import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private User user;
    private Boolean followedByAuthUser;
}
