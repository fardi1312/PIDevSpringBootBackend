package tn.esprit.pidevspringbootbackend.Services.Classes.Amira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Settings;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Amira.SettingsRepository;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Amira.ISettingsService;

import java.util.List;
import java.util.Optional;

@Service
public class SettingsService implements ISettingsService {

    @Autowired
    private SettingsRepository settingsRepository;

    @Override
    public List<Settings> getAllSettings() {
        return settingsRepository.findAll();
    }

    @Override
    public Optional<Settings> getSettingsById(Long id) {
        return Optional.empty();
    }


    @Override
    public Settings addSettings(Settings settings) {
        return settingsRepository.save(settings);
    }

    @Override
    public Settings updateSettings(Settings settings) {
        return settingsRepository.save(settings);
    }

    @Override
    public void deleteSettings(Long id) {
        settingsRepository.deleteById(id);
    }
}
