package tn.esprit.pidevspringbootbackend.RestControllers.Massoud;

import com.sun.tools.jconsole.JConsoleContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdatePasswordDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateProfilDTO;
import tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.jwt.UsersDetailsService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;




    @GetMapping("/account")
    public ResponseEntity<?> authenticatedUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByEmail(authentication.getName());
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                throw new UserNotFoundException("User not found");
            }
        } else {
            throw new IllegalStateException("User is not authenticated");
        }
    }

    @PostMapping("/account/update/info")
    public ResponseEntity<User> updateUserInfo(@Valid @RequestBody UpdateProfilDTO updateProfileDTO, Authentication authentication) {
        // Check if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Retrieve the user by email from authentication
            User user = userService.getUserByEmail(authentication.getName());
            if (user != null) {
                // Update user information with the provided DTO
                user = userService.updateUserInfo(user, updateProfileDTO);
                // Return the updated user information with HTTP status OK
                return ResponseEntity.ok(user);
            } else {
                // Throw exception if user not found
                throw new UserNotFoundException("User not found");
            }
        } else {
            // Throw exception if user is not authenticated
            throw new IllegalStateException("User is not authenticated");
        }
    }



    @DeleteMapping("/account/delete")
    public ResponseEntity<Map<String, String>> deleteUser(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            userService.deleteUser(user);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to delete user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/account/update/profilphoto")
    public ResponseEntity<String> updateProfilePhoto(Authentication authentication,
                                                     @RequestParam("profilePhoto") MultipartFile profilePhoto) {
        try {
            User user = userService.getUserByEmail(authentication.getName());

            if (profilePhoto.isEmpty()) {
                throw new IllegalArgumentException("Profile photo is empty");
            }

            userService.updateProfilPhoto(user, profilePhoto);

            return ResponseEntity.ok("Profile photo updated successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update profile photo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update profile photo: " + e.getMessage());
        }
    }

    @PutMapping("/account/update/coverphoto")
    public ResponseEntity<String> updateCoverPhoto(Authentication authentication,
                                                   @RequestParam("coverPhoto") MultipartFile coverPhoto) {
        try {
            // Retrieve the currently authenticated user
            User user = userService.getUserByEmail(authentication.getName());
            // Update the cover photo
            userService.updateCoverPhoto(user, coverPhoto);
            return ResponseEntity.ok("Cover photo updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update cover photo: " + e.getMessage());
        }
    }











    @PutMapping("/account/update/password")
    public ResponseEntity<String> updatePassword(Authentication authentication,
                                                 @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        try {
            // Retrieve the currently authenticated user
            User user = userService.getUserByEmail(authentication.getName());

            // Update the user's password
            userService.updatePassword(user, updatePasswordDTO);

            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update password: " + e.getMessage());
        }
    }
















    @GetMapping(value = "/account/profilePhoto", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProfilePhoto(Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            byte[] profilePhoto = userService.getProfilePhoto(user);
            System.out.println("image detected");
            return ResponseEntity.ok().body(profilePhoto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Failed to retrieve profile photo: " + e.getMessage()).getBytes());
        }
    }











}