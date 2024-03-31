package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;


import tn.esprit.pidevspringbootbackend.DTO.Massoud.SignupDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UserDTO;

public interface IAuthService {
    UserDTO createUser(SignupDTO signupDTO);
}
