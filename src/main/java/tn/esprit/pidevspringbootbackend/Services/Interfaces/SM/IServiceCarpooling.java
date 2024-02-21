package tn.esprit.pidevspringbootbackend.Services.Interfaces.SM;

import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;

public interface IServiceCarpooling {


    CarpoolingOffer  addCarpoolingOffer(Long id, CarpoolingOffer c);


    CarpoolingRequest addCarpoolingRequest(Long id, Long idO, CarpoolingRequest c);

    CarpoolingPreferences addCarpoolingPereferences(Long id, CarpoolingPreferences c);


    void deleteCarpoolingOffer(Long offerId);
}
