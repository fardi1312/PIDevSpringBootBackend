package tn.esprit.pidevspringbootbackend.RestControllers.Amira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Registration;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Amira.IRegistrationService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user/registration")
public class RegistrationController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRegistrationService iRegistrationService;
/*
    @PostMapping("/add")
    public ResponseEntity<Registration> addInscription(@RequestBody Registration registration) {
        Registration addedRegistration = iRegistrationService.addInscription(registration);
        return new ResponseEntity<>(addedRegistration, HttpStatus.CREATED);
    }*/
@PostMapping("/add")
public ResponseEntity<Registration> addRegistration(@RequestBody Registration registration) {
    try {
        // Retrieve the currently authenticated user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());

        // Set the authenticated user to the registration
        registration.setUser(currentUser);

        // Save the registration
        Registration addedRegistration = iRegistrationService.addInscription(registration);
        return new ResponseEntity<>(addedRegistration, HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @GetMapping("/all")
    public ResponseEntity<List<Registration>> getAllInscriptions() {
        List<Registration> registrations = iRegistrationService.getAllInscriptions();
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Registration> getInscriptionById(@PathVariable Long id) {
        Registration registration = iRegistrationService.getInscriptionById(id);
        if (registration != null) {
            return new ResponseEntity<>(registration, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getuserid/{id_user}")
    public ResponseEntity<List<Registration>> getRegistrationsByUserId(@PathVariable Long id_user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        List<Registration> registrations = iRegistrationService.getRegistrationsByUserId(user.getIdUser());
        if (!registrations.isEmpty()) {
            return new ResponseEntity<>(registrations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
   /* @PutMapping("update/{id}")
    public ResponseEntity<Registration> updateInscription(@PathVariable Long id, @RequestBody Registration registration) {
        // Check if the provided ID matches the ID of the registration entity
        if (!registration.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Call the service method to update the registration
        Registration updatedRegistration = iRegistrationService.updateInscription(registration);

        // Check if the registration was successfully updated
        if (updatedRegistration != null) {
            return new ResponseEntity<>(updatedRegistration, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

    @PutMapping("update/{id}")
    public ResponseEntity<Registration> updateInscription(@PathVariable Long id, @RequestBody Registration registration) {
        Registration updatedRegistration = iRegistrationService.updateInscription(registration);
        return new ResponseEntity<>(updatedRegistration, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        iRegistrationService.deleteInscription(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
