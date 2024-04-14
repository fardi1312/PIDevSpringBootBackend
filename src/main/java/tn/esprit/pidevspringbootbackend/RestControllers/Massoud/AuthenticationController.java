package tn.esprit.pidevspringbootbackend.RestControllers.Massoud;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.AuthenticationDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.AuthenticationResponse;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.UpdatePasswordDTO;
import tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.jwt.UsersDetailsService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IEmailService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;
import tn.esprit.pidevspringbootbackend.UserConfig.exception.UserNotFoundException;
import tn.esprit.pidevspringbootbackend.UserConfig.util.JwtUtil;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UsersDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsersDetailsService currentUserService;
    private final IUserService userService;
    private final IEmailService emailService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationDTO authenticationDTO) {
        User user = userService.getUserByEmail(authenticationDTO.getEmail());
        boolean emailVerified = user.getEmailVerified();
        boolean accountEnabled = user.getEnabled();
        if (!emailVerified || !accountEnabled) {
            emailService.send(user.getEmail(), "Email Verification", emailService.getMsgEmail(user));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not verified, bara chof lmail ta3k yhdik rabi ");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not activated");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDTO.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        currentUserService.saveCurrentUser(userDetails);
        System.out.println("username : " + userDetails.getUsername());
        String role = user.getRole().getType().toString();
        return ResponseEntity.ok(new AuthenticationResponse(jwt, role));
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        try {
            userService.forgotPassword(email);
            return ResponseEntity.ok("Password reset email sent successfully!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send password reset email!");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email,
                                                @RequestParam("token") String token,
                                                @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        try {
            return ResponseEntity.ok("Password reset successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reset password!");
        }
    }

    @GetMapping("/user/users/{idUser}")
    public ResponseEntity<User> getUserById(@PathVariable Long idUser) {
        User user = userService.getUserById(idUser);
        return ResponseEntity.ok(user);
    }
}
