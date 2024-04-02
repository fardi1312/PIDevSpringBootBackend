package tn.esprit.pidevspringbootbackend.Services.Interfaces.SM;

import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingType;

import java.util.Date;
import java.util.List;

public interface IServiceCarpooling {


    CarpoolingOffer  addCarpoolingOffer(Long id, CarpoolingOffer c);


    CarpoolingRequest addCarpoolingRequest(Long id, Long idO, CarpoolingRequest c,int nba, int nbr);

    CarpoolingPreferences addCarpoolingPereferences(Long id, CarpoolingPreferences c);




    CarpoolingOffer updateCarpoolingOffert(Long idO, CarpoolingOffer co);

    CarpoolingRequest updateCarpoolingrequest(Long idr, CarpoolingRequest crn);

    CarpoolingPreferences updateCarpoolingPreferences(Long idp, CarpoolingPreferences cpn);

    void deleteCarpoolingOffer(Long offerId);

    List<CarpoolingOffer> getall();

    List<CarpoolingOffer> getAllActiveOffers();

    List<CarpoolingOffer> findCarpoolingOffersByDateAndPrice(String date, Double price);

    List<CarpoolingOffer> searchCarpoolingOffer(String loc, CarpoolingType type, Date departureDate, Date returnDate);

    List<CarpoolingOffer> findMatchingOffers(Long id);

    PointCount updatePoint(Long idu, Long pcn);

    List<CarpoolingOffer> getCarpoolingById(long iduser);
}