package tn.esprit.pidevspringbootbackend.DAO.Repositories.Maram;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.EventRequests;

public interface EventRequestRepository  extends JpaRepository<EventRequests, Long> {
}
