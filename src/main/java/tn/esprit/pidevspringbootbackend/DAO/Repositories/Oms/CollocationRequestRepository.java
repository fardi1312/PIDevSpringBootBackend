package tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;

import java.util.List;

public interface CollocationRequestRepository extends JpaRepository<CollocationRequest,Long> {
    List<CollocationRequest> findByUser(User connectedUser);


}
