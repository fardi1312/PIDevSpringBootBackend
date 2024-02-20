package tn.esprit.pidevspringbootbackend.DAO.Repositories.SM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
@Repository
public interface RepoCarpoolingPreferences  extends JpaRepository<CarpoolingPreferences,Long> {
}
