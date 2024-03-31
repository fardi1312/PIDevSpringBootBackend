package tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CollocationOfferRepository extends JpaRepository<CollocationOffer,Long>  {
    List<CollocationOffer> findByGovernorateIgnoreCaseAndHouseTypeAndAvailablePlacesAndDateRent( String governorate, Integer houseType, Integer availablePlaces, Date dateRent);

    List<CollocationOffer> findByGovernorateIgnoreCaseAndHouseTypeAndAvailablePlaces(
            String governorate, Integer houseType, Integer availablePlaces);




    List<CollocationOffer> findByDateRent(Date dateRent);





    List<CollocationOffer> findByGovernorateIgnoreCaseAndHouseType(String governorate, Integer houseType);

    List<CollocationOffer> findByGovernorateIgnoreCaseAndAvailablePlaces(String governorate, Integer availablePlaces);





    List<CollocationOffer> findByGovernorateIgnoreCase(String governorate);

    List<CollocationOffer> findByHouseType(Integer houseType);

    List<CollocationOffer> findByAvailablePlaces(Integer availablePlaces);

    List<CollocationPreferences> findByUserIdUser(Long userId);


}
