package tn.esprit.pidevspringbootbackend.RestControllers.Massoud;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.SignupDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdateProfilDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UserDTO;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IAdminService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final IAdminService adminService;
    private final IUserService userService;


    @DeleteMapping("/user/{email}/delete") // Change userId to email
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String email) { // Change userId to email
        try {
            adminService.deleteUserByEmail(email); // Change method call to deleteUserByEmail
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to delete user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/user/{email}/disable") // Change userId to email
    public ResponseEntity<String> disableUserAccount(@PathVariable String email) { // Change userId to email
        try {
            adminService.disableUserAccount(email); // Change method call to disableUserAccount
            return ResponseEntity.ok("User account disabled successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to disable user account: " + e.getMessage());
        }
    }

    @PutMapping("/user/{email}/enable") // Change userId to email
    public ResponseEntity<String> enableUserAccount(@PathVariable String email) { // Change userId to email
        try {
            adminService.enableUserAccount(email); // Change method call to enableUserAccount
            return ResponseEntity.ok("User account enabled successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to enable user account: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = adminService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping("/user/count")
    public ResponseEntity<Integer> getTotalUserCount() {
        int totalCount = adminService.getTotalUserCount();
        return ResponseEntity.ok(totalCount);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO) {
        UserDTO createdUser = adminService.createAdmin(signupDTO);
        if (createdUser == null){
            return new ResponseEntity<>("User not created, come again later!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/update/info")
    public ResponseEntity<User> updateUserInfo(@Valid @RequestBody UpdateProfilDTO updateProfileDTO, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByEmail(authentication.getName());
            if (user != null) {
                user = adminService.updateAdminInfo(user, updateProfileDTO);
                return ResponseEntity.ok(user);
            } else {
                throw new UserNotFoundException("User not found");
            }
        } else {
            throw new IllegalStateException("User is not authenticated");
        }
    }



}
