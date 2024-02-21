package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationPreferencesRepository;

import java.util.List;

@Service
public class CollocationPreferencesService {
    @Autowired
    CollocationPreferencesRepository collocationPreferencesRepository ;
    public List<CollocationPreferences> getAllCollocationPreferences() {
        return collocationPreferencesRepository.findAll();
    }

    public CollocationPreferences getCollocationPreferencesById(long id) {
        return collocationPreferencesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CollocationPreferences not found with id: " + id));
    }

    public CollocationPreferences createCollocationPreferences(CollocationPreferences preferences) {
        return collocationPreferencesRepository.save(preferences);
    }
    public CollocationPreferences updateCollocationPreferences(long id, CollocationPreferences updatedPreferences) {
        CollocationPreferences existingPreferences = getCollocationPreferencesById(id);
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

    public void deleteCollocationPreferences(long id) {
        collocationPreferencesRepository.deleteById(id);
    }



}
