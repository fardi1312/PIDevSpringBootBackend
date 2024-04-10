package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Comment;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Post;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoPointCount;
import tn.esprit.pidevspringbootbackend.DAO.Response.UserResponse;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateEmailDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdatePasswordDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateProfilDTO;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IEmailService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.InvalidOperationException;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.UserNotFoundException;
import tn.esprit.pidevspringbootbackend.UserConfig.utilFiles.FileNamingUtil;
import tn.esprit.pidevspringbootbackend.UserConfig.utilFiles.FileUploadUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class UserService implements IUserService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  FileNamingUtil fileNamingUtil;
    @Autowired
    private  FileUploadUtil fileUploadUtil;
    @Autowired
    private  Environment environment;
    @Autowired
    private  RepoPointCount repoPointCount;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IEmailService emailService;


    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        emailService.sendResetPasswordEmail(email, newPassword);
    }



    @Override
    public User getUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        } catch (Exception e) {
            System.out.println("Error retrieving user by email: " + e.getMessage());
            throw new UserNotFoundException("User not found");
        }
    }
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
    @Override
    public User updateUserInfo(User user, UpdateProfilDTO updateProfilDTO) {
     if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        user.setFirstName(updateProfilDTO.getFirstName());
        user.setLastName(updateProfilDTO.getLastName());
        user.setIntro(updateProfilDTO.getIntro());
        user.setGender(updateProfilDTO.getGender());
        user.setHometown(updateProfilDTO.getHometown());
        user.setCurrentCity(updateProfilDTO.getCurrentCity());
        user.setEduInstitution(updateProfilDTO.getEduInstitution());
        user.setWorkplace(updateProfilDTO.getWorkplace());
        user.setBirthDate(updateProfilDTO.getBirthDate());
        user.setPhoneNumber(updateProfilDTO.getPhoneNumber());
        return userRepository.save(user);
    }
    @Override
    public void deleteUser(User user) {
        repoPointCount.delete(user.getPointCount());
        userRepository.delete(user);
    }
    @Override
    public User updateProfilePhoto(User user, MultipartFile profilePhoto) {
        if (profilePhoto != null && !profilePhoto.isEmpty() && profilePhoto.getSize() > 0) {
            try {
                String uploadDir = environment.getProperty("upload.user.images");
                String oldPhotoName = user.getProfilePhoto();
                String newPhotoName = fileNamingUtil.nameFile(profilePhoto);
                user.setProfilePhoto(newPhotoName);
                if (oldPhotoName != null) {
                    fileUploadUtil.deleteFile(uploadDir, oldPhotoName);
                }
                fileUploadUtil.saveNewFile(uploadDir, newPhotoName, profilePhoto);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update profile photo", e);
            }
        }
        return userRepository.save(user);
    }
    @Override
    public User updateCoverPhoto(User user, MultipartFile coverPhoto) {
        if (coverPhoto != null && !coverPhoto.isEmpty() && coverPhoto.getSize() > 0) {
            try {
                String uploadDir = environment.getProperty("upload.user.images");
                String oldPhotoName = user.getCoverPhoto();
                String newPhotoName = fileNamingUtil.nameFile(coverPhoto);
                user.setCoverPhoto(newPhotoName);
                if (oldPhotoName != null) {
                    fileUploadUtil.deleteFile(uploadDir, oldPhotoName);
                }
                fileUploadUtil.saveNewFile(uploadDir, newPhotoName, coverPhoto);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update profile photo", e);
            }
        }
        return userRepository.save(user);
    }
    @Override
    public String getPhotoUrlForConnectedUser(User connectedUser) {
        if (connectedUser != null) {
            String baseUrl = environment.getProperty("export.user.images");
            String photoFileName = connectedUser.getProfilePhoto();
            if (photoFileName != null && !photoFileName.isEmpty()) {
                return baseUrl  + photoFileName;
            }
        }
        return null;
    }
    @Override
    public String getCoverPhotoUrlForConnectedUser(User connectedUser) {
        if (connectedUser != null) {
            String baseUrl = environment.getProperty("export.user.images");
            String photoFileName = connectedUser.getCoverPhoto();
            if (photoFileName != null && !photoFileName.isEmpty()) {
                return baseUrl  + photoFileName;
            }
        }
        return null;
    }
    @Override
    public void updatePassword(User user, UpdatePasswordDTO updatePasswordDTO) {
        String encodedPassword = passwordEncoder.encode(updatePasswordDTO.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
    @Override
    public User updateEmail(User user, UpdateEmailDTO updateEmailDto) {
        if (!passwordEncoder.matches(updateEmailDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }
        user.setEmail(updateEmailDto.getEmail());
        return userRepository.save(user);
    }
    @Override
    public boolean verifyEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        user.setEmailVerified(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> getLikesByPostPaginate(Post post, Integer page, Integer size) {
        return userRepository.findUsersByLikedPosts(
                post,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
        );
    }

    @Override
    public List<User> getLikesByCommentPaginate(Comment comment, Integer page, Integer size) {
        return userRepository.findUsersByLikedComments(
                comment,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
        );
    }
}
