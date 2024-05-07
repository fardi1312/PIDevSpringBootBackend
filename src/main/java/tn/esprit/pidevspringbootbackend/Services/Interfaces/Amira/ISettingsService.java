package tn.esprit.pidevspringbootbackend.Services.Interfaces.Amira;

import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Settings;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Amira.SettingsRepository;

import java.util.List;
import java.util.Optional;

public interface ISettingsService {
    List<Settings> getAllSettings();
    Optional<Settings> getSettingsById(Long id);
    Settings addSettings(Settings settings);
    Settings updateSettings(Settings settings);
    void deleteSettings(Long id);
}
