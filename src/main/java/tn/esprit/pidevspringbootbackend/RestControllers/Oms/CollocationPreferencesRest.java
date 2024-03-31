package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationPreferencesService;
import org.springframework.web.bind.annotation.CrossOrigin;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;


import java.util.List;

@RestController
@RequestMapping("/api/collocationPreferences")
@CrossOrigin("*")
public class CollocationPreferencesRest {

    @Autowired
    private CollocationPreferencesService preferencesService;
    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<CollocationPreferences> savePreferences(@RequestBody CollocationPreferences preferences, @RequestParam("userId") long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        CollocationPreferences savedPreferences = preferencesService.saveCollocationPreferences(preferences, user.getIdUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPreferences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollocationPreferences> getPreferencesById(@PathVariable("id") long id) {
        CollocationPreferences preferences = preferencesService.getCollocationPreferencesById(id);
        return ResponseEntity.ok(preferences);
    }

    @GetMapping
    public ResponseEntity<List<CollocationPreferences>> getAllPreferences() {
        List<CollocationPreferences> preferencesList = preferencesService.getAllCollocationPreferences();
        return ResponseEntity.ok(preferencesList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollocationPreferences> updatePreferences(@PathVariable("id") long id, @RequestBody CollocationPreferences updatedPreferences) {
        CollocationPreferences preferences = preferencesService.updateCollocationPreferences(id, updatedPreferences);
        return ResponseEntity.ok(preferences);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreferences(@PathVariable("id") long id) {
        preferencesService.deleteCollocationPreferences(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CollocationPreferences>> getCollocationPreferencesByUserId(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        List<CollocationPreferences> preferences = preferencesService.getCollocationPreferencesByUserId(user.getIdUser());
        return ResponseEntity.ok(preferences);
    }










}
