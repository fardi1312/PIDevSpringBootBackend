package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Comment;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Post;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Response.UserResponse;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateEmailDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdatePasswordDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateProfilDTO;

import java.util.List;

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

    List<User> getLikesByPostPaginate(Post targetPost, Integer page, Integer size);

    List<User> getLikesByCommentPaginate(Comment targetComment, Integer page, Integer size);


    List<UserResponse> getUserSearchResult(String key, Integer page, Integer size);

    UserResponse userToUserResponse(User user);

    User getAuthenticatedUser();


    void requestAccountVerification(String subscriptionType);

    String generateVerificationToken();


    boolean isAccountVerified();

    //isAccountVerifiedByidUser
    boolean isAccountVerifiedByidUser(Long userId);
}
