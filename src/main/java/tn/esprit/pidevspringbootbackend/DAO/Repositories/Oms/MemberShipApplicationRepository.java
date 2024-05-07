package tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.Club;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.MemberShipApplication;

public interface MemberShipApplicationRepository extends JpaRepository<MemberShipApplication,Long>{
}
