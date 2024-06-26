package tn.esprit.pidevspringbootbackend.DAO.Repositories.SM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingStatus;

import java.util.Date;
import java.util.List;

@Repository
public interface RepoCarpoolingOffer extends JpaRepository<CarpoolingOffer, Long> {

    @Query("SELECT c FROM CarpoolingOffer c WHERE c.location = :location")
    List<CarpoolingOffer> findByLocation(String location);

    @Query("SELECT c FROM CarpoolingOffer c WHERE (c.DateAller = :dateAller OR c.DateRetour = :dateRetour) AND c.price = :price")
    List<CarpoolingOffer> findByDateAllerOrDateRetourAndPrice(String dateAller, String dateRetour, Double price);

    @Query("SELECT c FROM CarpoolingOffer c WHERE c.DateAller = :dateAller OR c.DateRetour = :dateRetour")
    List<CarpoolingOffer> findByDateAllerOrDateRetour(String dateAller, String dateRetour);

    @Query("SELECT c FROM CarpoolingOffer c WHERE c.price = :price")
    List<CarpoolingOffer> findByPrice(Double price);
    List<CarpoolingOffer> findByCarpoolingStatus(CarpoolingStatus carpoolingStatus);

    List<CarpoolingOffer> findByUserO(User connectedUser);
}
