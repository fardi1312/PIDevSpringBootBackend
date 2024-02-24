package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationPreferencesRepository;

import java.util.List;

@Service
public class CollocationPreferencesService {

    @Autowired
    private CollocationPreferencesRepository collocationPreferencesRepository;

    @Autowired
    private UserRepository userRepository;

    public CollocationPreferences getAllCollocationPreferencesByUserId(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        return user.getCollocationPreferences();
    }

    public CollocationPreferences getCollocationPreferencesByIdUser(long userId) {
        CollocationPreferences preferences;
        preferences = userRepository.getReferenceById(userId).getCollocationPreferences();
        return preferences;
    }

    public CollocationPreferences createCollocationPreferences(long userId, CollocationPreferences preferences) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (user.getCollocationPreferences() != null) {
            throw new IllegalArgumentException("CollocationPreferences already exist for the user with id: " + userId);
        }

        preferences.setUser(user);
        return collocationPreferencesRepository.save(preferences);
    }

    public CollocationPreferences updateCollocationPreferences(long userId, long collocationPreferencesId, CollocationPreferences updatedPreferences) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        CollocationPreferences existingPreferences = user.getCollocationPreferences();

        if (existingPreferences == null || existingPreferences.getIdCollocationPreferences() != collocationPreferencesId) {
            throw new EntityNotFoundException("CollocationPreferences not found with id: " + collocationPreferencesId);
        }

        // Update the existing preferences with the new values
        existingPreferences.setPets(updatedPreferences.getPets());
        existingPreferences.setSmoking(updatedPreferences.getSmoking());
        existingPreferences.setBudget(updatedPreferences.getBudget());
        existingPreferences.setGender(updatedPreferences.getGender());
        existingPreferences.setInterest(updatedPreferences.getInterest());
        existingPreferences.setRoomType(updatedPreferences.getRoomType());
        existingPreferences.setHouseType(updatedPreferences.getHouseType());
        existingPreferences.setLocation(updatedPreferences.getLocation());

        return collocationPreferencesRepository.save(existingPreferences);
    }

    public void deleteCollocationPreferences(long userId, long collocationPreferencesId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        CollocationPreferences existingPreferences = user.getCollocationPreferences();

        if (existingPreferences == null || existingPreferences.getIdCollocationPreferences() != collocationPreferencesId) {
            throw new EntityNotFoundException("CollocationPreferences not found with id: " + collocationPreferencesId);
        }

        collocationPreferencesRepository.delete(existingPreferences);
    }

}
