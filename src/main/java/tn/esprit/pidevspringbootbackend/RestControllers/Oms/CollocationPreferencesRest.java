package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationPreferencesService;
import org.springframework.web.bind.annotation.CrossOrigin;



import java.util.List;

@RestController
@RequestMapping("/api/collocationPreferences")
@CrossOrigin("*")
public class CollocationPreferencesRest {

    @Autowired
    private CollocationPreferencesService preferencesService;

    @PostMapping
    public ResponseEntity<CollocationPreferences> savePreferences(@RequestBody CollocationPreferences preferences, @RequestParam("userId") long userId) {
        CollocationPreferences savedPreferences = preferencesService.saveCollocationPreferences(preferences, userId);
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
        List<CollocationPreferences> preferences = preferencesService.getCollocationPreferencesByUserId(userId);
        return ResponseEntity.ok(preferences);
    }

    @GetMapping("/user/{userId}/{collocationPreferencesId}")
    public ResponseEntity<CollocationPreferences> getCollocationPreferencesById(@PathVariable long userId) {
        CollocationPreferences preferences = collocationPreferencesService.getCollocationPreferencesByIdUser(userId);
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
