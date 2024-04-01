package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
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


    public CollocationPreferences saveCollocationPreferences(CollocationPreferences preferences, long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        preferences.setUser(user);
        return collocationPreferencesRepository.save(preferences);
    }
    public List<CollocationPreferences> getCollocationPreferencesByUserId(Long userId) {
        return collocationPreferencesRepository.findByUserIdUser(userId);
    }


    public CollocationPreferences getCollocationPreferencesById(long id) {
        return collocationPreferencesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preferences not found with id: " + id));
    }

    public List<CollocationPreferences> getAllCollocationPreferences() {
        return collocationPreferencesRepository.findAll();
    }

    public void deleteCollocationPreferences(long id) {
        collocationPreferencesRepository.deleteById(id);
    }

    public CollocationPreferences updateCollocationPreferences(long id, CollocationPreferences updatedPreferences) {
        CollocationPreferences existingPreferences = collocationPreferencesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preferences not found with id: " + id));

        existingPreferences.setPets(updatedPreferences.getPets());
        existingPreferences.setSmoking(updatedPreferences.getSmoking());
        existingPreferences.setBudget(updatedPreferences.getBudget());
        existingPreferences.setGender(updatedPreferences.getGender());
        existingPreferences.setInterest(updatedPreferences.getInterest());
        existingPreferences.setRoomType(updatedPreferences.getRoomType());
        existingPreferences.setHouseType(updatedPreferences.getHouseType());
        existingPreferences.setLocation(updatedPreferences.getLocation());
        // Set other properties as needed

        return collocationPreferencesRepository.save(existingPreferences);
    }
}
