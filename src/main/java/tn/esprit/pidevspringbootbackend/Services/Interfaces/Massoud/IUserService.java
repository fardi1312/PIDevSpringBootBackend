package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateEmailDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdatePasswordDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateProfilDTO;
public interface IUserService {
    void forgotPassword(String email);

    User getUserByEmail(String email);
    User getUserById(Long userId);
    User updateUserInfo(User user, UpdateProfilDTO updateProfilDTO);
    void deleteUser(User user);
    void updatePassword(User user, UpdatePasswordDTO updatePasswordDTO);
    User updateProfilePhoto(User user, MultipartFile profilePhoto);
    User updateCoverPhoto(User user, MultipartFile coverPhoto) ;
    String getPhotoUrlForConnectedUser(User connectedUser);
    String getCoverPhotoUrlForConnectedUser(User connectedUser);
    User updateEmail(User user, UpdateEmailDTO updateEmailDto);
    boolean verifyEmail(String email);
}
