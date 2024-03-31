package tn.esprit.pidevspringbootbackend.Services.Classes.SM;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingStatus;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingType;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoPointCount;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.SM.IServiceCarpooling;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ServicesCarpooling implements IServiceCarpooling {



    @Autowired
    RepoCarpoolingOffer repoCarpoolingOffer ;
    @Autowired
    RepoCarpoolingPreferences repoCarpoolingPreferences ;
    @Autowired
    RepoCarpoolingRequest repoCarpoolingRequest ;
    @Autowired
    RepoPointCount repoPointCount;
    @Autowired
    UserRepository repoUser ;




    @Override
    public CarpoolingOffer  addCarpoolingOffer(Long id, CarpoolingOffer c){
        User u = repoUser.findById(id).orElse(null);
        List<CarpoolingOffer> l = u.getCarpoolingOffers();
        if(l==null){
            List<CarpoolingOffer> ln =new ArrayList<CarpoolingOffer>();
            ln.add(c);
            u.setCarpoolingOffers(ln);

        }
        l.add(c);
        u.setCarpoolingOffers(l);
        c.setUserO(u);
//repoUser.save(u);
        return repoCarpoolingOffer.save(c);

    }

    @Override
    public  CarpoolingRequest addCarpoolingRequest(Long id, Long idO, CarpoolingRequest c,int nba, int nbr){
        c.setNbPlacesRetour(nbr);
        c.setNbPlacesAller(nba);

        CarpoolingOffer o = repoCarpoolingOffer.findById(idO).orElse(null);
        User u = repoUser.findById(id).orElse(null);
        PointCount pc = u.getPointCount();


        if (c.getNbPlacesAller()<=o.getPlaceDispoAller() && c.getNbPlacesRetour()<=o.getPlaceDispoRetour()) {
            double priceF = o.getPrice() * (c.getNbPlacesRetour() + c.getNbPlacesAller());
            if (pc.getNbPoint() >= priceF) {
                o.setPlaceDispoAller(o.getPlaceDispoAller()-c.getNbPlacesAller());
                o.setPlaceDispoRetour(o.getPlaceDispoRetour()-c.getNbPlacesRetour());

                pc.setNbPoint(pc.getNbPoint() - priceF);
                o.getUserO().getPointCount().setNbPoint(o.getUserO().getPointCount().getNbPoint() + priceF);

                o.getCarpoolingRequests().add(c);
                c.setCarpoolingoffer(o);
                u.getCarpoolingRequests().add(c);
                c.setUserR(u);

                if( o.getPlaceDispoAller()==0 && o.getPlaceDispoRetour()==0 ){


                    o.setCarpoolingStatus( CarpoolingStatus.Expired );
                }
                if (   o.getPlaceDispoRetour()==0 ){

                    if ( o.getCarpoolingType()==CarpoolingType.Retour){

                        o.setCarpoolingStatus(CarpoolingStatus.Expired);
                    }

                    o.setCarpoolingType( CarpoolingType.Aller);
                }

                if (   o.getPlaceDispoAller()==0 ){

                    if ( o.getCarpoolingType()==CarpoolingType.Aller){

                        o.setCarpoolingStatus(CarpoolingStatus.Expired);
                    }

                    o.setCarpoolingType( CarpoolingType.Retour);
                }

                if (c.getNbPlacesRetour()==0){
                    c.setRequestType(CarpoolingType.Aller);

                }
                if (c.getNbPlacesAller()==0){
                    c.setRequestType(CarpoolingType.Retour);

                }

                return repoCarpoolingRequest.save(c);

            }
        }





        return null ;
    }

    @Override
    public CarpoolingPreferences addCarpoolingPereferences(Long id, CarpoolingPreferences c){

        User u = repoUser.findById(id).orElse(null);
        u.setCarpoolingPreferences(c);
        c.setUserS(u);

        return repoCarpoolingPreferences.save(c);

    }

    @Override
    public void deleteCarpoolingOffer(Long offerId) {
        CarpoolingOffer co = repoCarpoolingOffer.findById(offerId).orElse(null);

        User user = co.getUserO();
        if (user != null) {

            List<CarpoolingOffer> offers = user.getCarpoolingOffers();

            offers.remove(co);
            user.setCarpoolingOffers(offers);
            repoCarpoolingOffer.delete(co);

        }
    }
    @Override

    public CarpoolingOffer updateCarpoolingOffert(Long idO, CarpoolingOffer con){
        CarpoolingOffer coo = repoCarpoolingOffer.findById(idO).orElse(null);
        coo.setLocation(con.getLocation());
        coo.setImg(con.getImg());
        coo.setCarpoolingStatus(con.getCarpoolingStatus());
        coo.setForWho(con.getForWho());
        coo.setCarpoolingType(con.getCarpoolingType());
        coo.setPlaceDispoAller(con.getPlaceDispoAller());
        coo.setPlaceDispoRetour(con.getPlaceDispoRetour());
        coo.setPrice(con.getPrice());
        coo.setChauffage(con.getChauffage());
        coo.setClimatise(con.getClimatise());
        coo.setRadioon(con.getRadioon());
        coo.setSmoking(con.getSmoking());
        coo.setDescription(con.getDescription());
        coo.setDateAller(con.getDateAller());
        coo.setDateRetour(con.getDateRetour());
        coo.setCarpoolingStatus(con.getCarpoolingStatus());
        coo.setOfferDate(con.getOfferDate());
        coo.setLocationLx(con.getLocationLx());
        coo.setLocationLy(con.getLocationLy());
        coo.setTargets(con.getTargets());



        return repoCarpoolingOffer.save(coo)    ;
    }
    @Override
    public  CarpoolingRequest updateCarpoolingrequest(Long idr, CarpoolingRequest crn){
        return null;
    }
    @Override
    public CarpoolingPreferences updateCarpoolingPreferences(Long userId, CarpoolingPreferences updatedPreferences) {

        User user = repoUser.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }


        CarpoolingPreferences existingPreferences = user.getCarpoolingPreferences();
        if (existingPreferences == null) {
            return null;
        }


        if (updatedPreferences.getBudget() != null) {
            existingPreferences.setBudget(updatedPreferences.getBudget());
        }
        if (updatedPreferences.getGender() != null) {
            existingPreferences.setGender(updatedPreferences.getGender());
        }
        if (updatedPreferences.getCarpoolingType() != null) {
            existingPreferences.setCarpoolingType(updatedPreferences.getCarpoolingType());
        }
        if (updatedPreferences.getNbPlaces() != null) {
            existingPreferences.setNbPlaces(updatedPreferences.getNbPlaces());
        }
        if (updatedPreferences.getLocation() != null) {
            existingPreferences.setLocation(updatedPreferences.getLocation());
        }
        if (updatedPreferences.getDescription() != null) {
            existingPreferences.setDescription(updatedPreferences.getDescription());
        }
        if (updatedPreferences.getRadioon() != null) {
            existingPreferences.setRadioon(updatedPreferences.getRadioon());
        }
        if (updatedPreferences.getClimatise() != null) {
            existingPreferences.setClimatise(updatedPreferences.getClimatise());
        }
        if (updatedPreferences.getChauffage() != null) {
            existingPreferences.setChauffage(updatedPreferences.getChauffage());
        }
        if (updatedPreferences.getSmoking() != null) {
            existingPreferences.setSmoking(updatedPreferences.getSmoking());
        }


        return repoCarpoolingPreferences.save(existingPreferences);
    }
    @Override
    public   List<CarpoolingOffer> getall(){


        return repoCarpoolingOffer.findAll();
    }

    @Override
    public List<CarpoolingOffer> getAllActiveOffers() {
        List<CarpoolingOffer> allOffers = repoCarpoolingOffer.findAll();
        List<CarpoolingOffer> activeOffers = new ArrayList<>();

        for (CarpoolingOffer offer : allOffers) {
            if (offer.getCarpoolingStatus() == CarpoolingStatus.Active) {
                activeOffers.add(offer);
            }
        }

        return activeOffers;
    }
    @Override
    public List<CarpoolingOffer> findCarpoolingOffersByDateAndPrice(String date, Double price) {
        if (date == null && price == null) {
            return repoCarpoolingOffer.findAll();
        } else if (date != null && price != null) {
            List<CarpoolingOffer> allOffers = repoCarpoolingOffer.findAll();
            List<CarpoolingOffer> filteredOffers = new ArrayList<>();
            for (CarpoolingOffer offer : allOffers) {
                if (compareDatesWithTolerance(offer.getDateAller(), date) ||
                        compareDatesWithTolerance(offer.getDateRetour(), date) ||
                        offer.getPrice() == price) {
                    System.out.println("gvvvvvvvvvvvvv");
                    filteredOffers.add(offer);
                }
            }
            return filteredOffers;
        } else if (date != null) {
            List<CarpoolingOffer> allOffers = repoCarpoolingOffer.findAll();
            List<CarpoolingOffer> filteredOffers = new ArrayList<>();
            for (CarpoolingOffer offer : allOffers) {
                if (compareDatesWithTolerance(offer.getDateAller(), date) ||
                        compareDatesWithTolerance(offer.getDateRetour(), date)) {
                    filteredOffers.add(offer);
                }
            }
            return filteredOffers;
        } else {
            return repoCarpoolingOffer.findByPrice(price);
        }
    }

    private boolean compareDatesWithTolerance(Date date1, String date2String) {
        if (date1 == null) {
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date2 = dateFormat.parse(date2String);

            long diffInMillis = Math.abs(date1.getTime() - date2.getTime());
            long toleranceInMillis = 2 * 60 * 60 * 1000;
            return diffInMillis <= toleranceInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }





    @Override
    public List<CarpoolingOffer> searchCarpoolingOffer(String loc,CarpoolingType type, Date departureDate, Date returnDate) {
        return  null;
    }

    @Override
    public List<CarpoolingOffer> findMatchingOffers(Long id) {
        User u = repoUser.findById(id).orElse(null);
        CarpoolingPreferences preferences = u.getCarpoolingPreferences();
        List<CarpoolingOffer> allOffers = repoCarpoolingOffer.findAll();
        List<CarpoolingOffer> All = new ArrayList<>();

        for (CarpoolingOffer offer : allOffers) {
            if (offer.getCarpoolingStatus() == CarpoolingStatus.Active) {
                All.add(offer);
            }
        }
        //  List<CarpoolingOffer> All = repoCarpoolingOffer.findAll();
        Map<Integer, List<CarpoolingOffer>> offersByConditionsMet = new TreeMap<>(Collections.reverseOrder());

        for (CarpoolingOffer offer : All) {
            int conditionsMet = 0;

            if (offer.getLocation().equalsIgnoreCase(preferences.getLocation())) {
                conditionsMet = conditionsMet + 1000;
            }

            if (offer.getCarpoolingType() == CarpoolingType.Aller) {
                if (offer.getPlaceDispoAller() == preferences.getNbPlaces()) {
                    conditionsMet++;
                }
            } else if (offer.getCarpoolingType() == CarpoolingType.Retour) {
                if (offer.getPlaceDispoRetour() == preferences.getNbPlaces()) {
                    conditionsMet++;
                }
            } else if (offer.getCarpoolingType() == CarpoolingType.Aller_et_Retour) {
                if (offer.getPlaceDispoRetour() == preferences.getNbPlaces() && offer.getPlaceDispoAller() == preferences.getNbPlaces()) {
                    conditionsMet++;
                }
            }

            if (offer.getPrice() >= preferences.getBudget() - 10 && offer.getPrice() <= preferences.getBudget() + 10) {
                conditionsMet+= 10;
            }
            if (offer.getChauffage() == preferences.getChauffage()) {
                conditionsMet++;
            }
            if (offer.getClimatise() == preferences.getClimatise()) {
                conditionsMet++;
            }
            if (offer.getSmoking() == preferences.getSmoking()) {
                conditionsMet++;
            }


            if (offer.getRadioon() == (preferences.getRadioon())) {
                conditionsMet++;
            }

            // Group offers by the number of conditions met
            offersByConditionsMet.computeIfAbsent(conditionsMet, k -> new ArrayList<>()).add(offer);
        }

        List<CarpoolingOffer> matchingOffers = new ArrayList<>();

        // Add offers to the matchingOffers list in order of conditions met
        for (List<CarpoolingOffer> offers : offersByConditionsMet.values()) {
            matchingOffers.addAll(offers);
        }

        return matchingOffers;
    }



    @Override
    public  PointCount updatePoint(Long idu , Long pcn){

        User u = repoUser.findById(idu).orElse(null);
        PointCount pco = u.getPointCount();

        pco.setNbPoint(pcn+pco.getNbPoint());

        return  repoPointCount.save(pco);
    }
}