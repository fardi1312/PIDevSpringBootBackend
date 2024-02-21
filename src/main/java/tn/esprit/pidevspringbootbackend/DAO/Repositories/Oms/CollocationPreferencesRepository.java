package tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;

public interface CollocationPreferencesRepository extends JpaRepository<CollocationPreferences,Long> {
}
