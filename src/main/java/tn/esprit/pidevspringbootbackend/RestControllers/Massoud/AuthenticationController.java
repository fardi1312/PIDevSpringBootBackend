package tn.esprit.pidevspringbootbackend.RestControllers.Massoud;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.AuthenticationDTO;
import tn.esprit.pidevspringbootbackend.DTO.Massoud.AuthenticationResponse;
import tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.UserService;
import tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.jwt.UsersDetailsService;
import tn.esprit.pidevspringbootbackend.UserConfig.util.JwtUtil;

@RestController

public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UsersDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsersDetailsService currentUserService;



    public AuthenticationController(AuthenticationManager authenticationManager, UsersDetailsService userDetailsService, JwtUtil jwtUtil, UsersDetailsService currentUserService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.currentUserService = currentUserService;

    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationDTO authenticationDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not activated");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDTO.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        // Save user details after successful authentication
        currentUserService.saveCurrentUser(userDetails);
        // affeche username
        System.out.println("username : " + userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }






}
