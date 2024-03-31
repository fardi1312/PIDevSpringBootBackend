package tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;

import java.util.List;

public interface CollocationPreferencesRepository extends JpaRepository<CollocationPreferences,Long> {
    List<CollocationPreferences> findByUserIdUser(Long userId);

}

