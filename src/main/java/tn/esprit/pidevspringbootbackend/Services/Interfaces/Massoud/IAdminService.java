package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;

import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.SignupDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateProfilDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IAdminService {

    void deleteUserByEmail(String email);
    void disableUserAccount(String email);
    void enableUserAccount(String email);
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);
    int getTotalUserCount();

    UserDTO createAdmin(SignupDTO signupDTO);

    User updateAdminInfo(User user, UpdateProfilDTO updateProfilDTO);



}
