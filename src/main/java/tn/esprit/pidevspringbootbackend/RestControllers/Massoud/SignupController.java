package tn.esprit.pidevspringbootbackend.RestControllers.Massoud;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.SignupDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UserDTO;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IAuthService;

@RestController
public class SignupController {
    @Autowired
    private IAuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO) {
       UserDTO createdUser = authService.createUser(signupDTO);
       if (createdUser == null){
           return new ResponseEntity<>("User not created, come again later!", HttpStatus.BAD_REQUEST);
       }
       return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email) {
        boolean emailVerified = authService.verifyEmail(email);
        if (emailVerified) {
            return ResponseEntity.ok("Email verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email verification failed. User not found.");
        }
    }



}
