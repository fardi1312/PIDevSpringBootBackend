package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationPreferencesService;


@RestController
@RequestMapping("/api/collocationPreferences")
public class CollocationPreferencesRest {

    @Autowired
    private CollocationPreferencesService collocationPreferencesService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CollocationPreferences> getAllCollocationPreferencesByUserId(@PathVariable long userId) {
        CollocationPreferences preferences = collocationPreferencesService.getAllCollocationPreferencesByUserId(userId);
        return new ResponseEntity<>(preferences, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/{collocationPreferencesId}")
    public ResponseEntity<CollocationPreferences> getCollocationPreferencesById(@PathVariable long userId) {
        CollocationPreferences preferences = collocationPreferencesService.getCollocationPreferencesById(userId);
        return new ResponseEntity<>(preferences, HttpStatus.OK);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<CollocationPreferences> createCollocationPreferences(@PathVariable long userId, @RequestBody CollocationPreferences preferences) {
        CollocationPreferences createdPreferences = collocationPreferencesService.createCollocationPreferences(userId, preferences);
        return new ResponseEntity<>(createdPreferences, HttpStatus.CREATED);
    }

    @PutMapping("/user/{userId}/{collocationPreferencesId}")
    public ResponseEntity<CollocationPreferences> updateCollocationPreferences(@PathVariable long userId, @PathVariable long collocationPreferencesId, @RequestBody CollocationPreferences updatedPreferences) {
        CollocationPreferences updatedPreferencesResult = collocationPreferencesService.updateCollocationPreferences(userId, collocationPreferencesId, updatedPreferences);
        return new ResponseEntity<>(updatedPreferencesResult, HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/{collocationPreferencesId}")
    public ResponseEntity<Void> deleteCollocationPreferences(@PathVariable long userId, @PathVariable long collocationPreferencesId) {
        collocationPreferencesService.deleteCollocationPreferences(userId, collocationPreferencesId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
