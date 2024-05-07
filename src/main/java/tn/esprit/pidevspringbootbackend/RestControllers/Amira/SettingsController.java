package tn.esprit.pidevspringbootbackend.RestControllers.Amira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Settings;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Amira.ISettingsService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    @Autowired
    private ISettingsService settingsService;

    @GetMapping
    public ResponseEntity<List<Settings>> getAllSettings() {
        List<Settings> settingsList = settingsService.getAllSettings();
        return new ResponseEntity<>(settingsList, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<Settings> addSettings(@RequestBody Settings settings) {
        Settings addedSettings = settingsService.addSettings(settings);
        return new ResponseEntity<>(addedSettings, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Settings> updateSettings(@PathVariable Long id, @RequestBody Settings settings) {
        settings.setId(id);
        Settings updatedSettings = settingsService.updateSettings(settings);
        return new ResponseEntity<>(updatedSettings, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSettings(@PathVariable Long id) {
        settingsService.deleteSettings(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
