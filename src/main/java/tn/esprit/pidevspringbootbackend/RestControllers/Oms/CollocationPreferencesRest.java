package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationPreferencesService;

import java.util.List;

@RestController
@RequestMapping("/api/collocation/preferences")

public class CollocationPreferencesRest {
    @Autowired
    CollocationPreferencesService collocationPreferencesService;
    @GetMapping
    public ResponseEntity<List<CollocationPreferences>> getAllCollocationPreferences() {
        List<CollocationPreferences> preferencesList = collocationPreferencesService.getAllCollocationPreferences();
        return new ResponseEntity<List<CollocationPreferences>>(preferencesList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollocationPreferences> getCollocationPreferencesById(@PathVariable long id) {
        CollocationPreferences preferences = collocationPreferencesService.getCollocationPreferencesById(id);
        return new ResponseEntity<>(preferences, HttpStatus.OK);

    }
    @PostMapping
    public ResponseEntity<CollocationPreferences> createCollocationPreferences(@RequestBody CollocationPreferences preferences) {
        CollocationPreferences createdPreferences = collocationPreferencesService.createCollocationPreferences(preferences);
        return new ResponseEntity<>(createdPreferences, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollocationPreferences> updateCollocationPreferences(@PathVariable long id, @RequestBody CollocationPreferences updatedPreferences) {
        CollocationPreferences preferences = collocationPreferencesService.updateCollocationPreferences(id, updatedPreferences);
        return new ResponseEntity<>(preferences, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollocationPreferences(@PathVariable long id) {
        collocationPreferencesService.deleteCollocationPreferences(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
