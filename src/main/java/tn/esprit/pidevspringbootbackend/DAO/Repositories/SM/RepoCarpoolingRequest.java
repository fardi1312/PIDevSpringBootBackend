package tn.esprit.pidevspringbootbackend.DAO.Repositories.SM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;

import java.util.List;

@Repository
public interface RepoCarpoolingRequest  extends JpaRepository<CarpoolingRequest,Long> {

    List<CarpoolingRequest> findByUserR(User connectedUser);

}
