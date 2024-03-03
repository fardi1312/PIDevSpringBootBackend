package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;


import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdatePasswordDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateProfilDTO;

public interface IUserService {
    User getUserByEmail(String email);

    User getUserById(Long userId);

    User updateUserInfo(User user, UpdateProfilDTO updateProfilDTO);

    //delete user
     void deleteUser(User user);
    User updateProfilPhoto(User user, MultipartFile profilePhoto);


    void updatePassword(User user, UpdatePasswordDTO updatePasswordDTO);

    User updateCoverPhoto(User user, MultipartFile coverPhoto);

    byte[] getProfilePhoto(User user);







}
