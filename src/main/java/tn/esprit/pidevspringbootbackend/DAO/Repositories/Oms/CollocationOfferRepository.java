package tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;

import java.util.List;

public interface CollocationOfferRepository extends JpaRepository<CollocationOffer,Long>  {
    List<CollocationOffer> findByUser(User user);

}
