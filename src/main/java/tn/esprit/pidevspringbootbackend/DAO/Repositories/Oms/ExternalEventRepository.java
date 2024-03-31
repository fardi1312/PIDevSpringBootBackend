package tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.ExternalEvent;

public interface ExternalEventRepository extends JpaRepository<ExternalEvent, Long> {
}
