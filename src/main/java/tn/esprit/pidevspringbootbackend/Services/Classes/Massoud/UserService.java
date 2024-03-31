package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoPointCount;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdatePasswordDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateProfilDTO;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.UserNotFoundException;
import tn.esprit.pidevspringbootbackend.UserConfig.utilFiles.FileNamingUtil;
import tn.esprit.pidevspringbootbackend.UserConfig.utilFiles.FileUploadUtil;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;
    private final Environment environment;
    private final RepoPointCount repoPointCount;


    @Autowired
    private PasswordEncoder passwordEncoder;


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
    // Assuming the user is already authenticated and obtained from the provided 'user' parameter
    // You may want to remove the hardcoded user retrieval logic if not needed
    if (user == null) {
        throw new IllegalArgumentException("User cannot be null");
    }

    // Update user information with data from the UpdateProfilDTO
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
    // Save the updated user object
    return userRepository.save(user);
}


    @Override
    public void deleteUser(User user) {
        // Delete the associated point count
        repoPointCount.delete(user.getPointCount());
        // Delete the user
        userRepository.delete(user);
    }



    @Override
    public User updateProfilPhoto(User user, MultipartFile profilePhoto) {
        if (profilePhoto != null && !profilePhoto.isEmpty() && profilePhoto.getSize() > 0) {
            try {
                String uploadDir = environment.getProperty("upload.user.images");
                String oldPhotoName = user.getProfilePhoto();
                String newPhotoName = fileNamingUtil.nameFile(profilePhoto);
                String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                        + uploadDir + File.separator + newPhotoName;

                user.setProfilePhoto(newPhotoUrl);

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
                String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                        + uploadDir + File.separator + newPhotoName;

                user.setCoverPhoto(newPhotoUrl);

                if (oldPhotoName == null) {
                    fileUploadUtil.saveNewFile(uploadDir, newPhotoName, coverPhoto);
                } else {
                    fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, coverPhoto);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to update cover photo", e);
            }
        }

        return userRepository.save(user);
    }

    @Override
    public void updatePassword(User user, UpdatePasswordDTO updatePasswordDTO) {
        // Encode the new password using the configured PasswordEncoder bean
        String encodedPassword = passwordEncoder.encode(updatePasswordDTO.getNewPassword());
        // Set the encoded password to the user
        user.setPassword(encodedPassword);
        // Save the updated user
        userRepository.save(user);
    }








    @Override
    public byte[] getProfilePhoto(User user) {
        if (user.getProfilePhoto() != null) {
            try {
                // Extracting path from URL
                String photoUrl = user.getProfilePhoto();
                String uploadDir = environment.getProperty("upload.user.images");
                String fileName = photoUrl.substring(photoUrl.lastIndexOf('/') + 1);
                Path filePath = Paths.get(uploadDir, fileName);

                return Files.readAllBytes(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to retrieve profile photo", e);
            }
        } else {
            throw new IllegalArgumentException("User does not have a profile photo");
        }
    }







}
