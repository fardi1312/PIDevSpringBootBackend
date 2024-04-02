package tn.esprit.pidevspringbootbackend.RestControllers.Amira;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.EmailRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import org.springframework.security.core.context.SecurityContextHolder;
import tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.email.EmailService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;


@RestController
public class EmailController {
    @Autowired
    private IUserService  userService;
    @Autowired
    private EmailService emailService;
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        try {
            emailService.sendEmail1(user.getEmail(), emailRequest.getSubject(), emailRequest.getBody());
            return ResponseEntity.ok("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
