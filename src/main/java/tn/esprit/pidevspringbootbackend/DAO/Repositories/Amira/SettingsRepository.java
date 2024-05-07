package tn.esprit.pidevspringbootbackend.DAO.Repositories.Amira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Settings;

@Repository
public interface SettingsRepository extends JpaRepository<Settings,Long> {
}
