package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;


import org.springframework.security.oauth2.core.user.OAuth2User;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.SignupDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UserDTO;

public interface IAuthService {
    UserDTO createUser(SignupDTO signupDTO);
    boolean verifyEmail(String email);

    UserDTO createUserFromOAuth2(OAuth2User oauth2User);
}
