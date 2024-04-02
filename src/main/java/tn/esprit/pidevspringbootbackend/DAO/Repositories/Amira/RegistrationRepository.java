package tn.esprit.pidevspringbootbackend.DAO.Repositories.Amira;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Registration;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration,Long> {
    List<Registration> findByUserIdUser(Long userId);

}
