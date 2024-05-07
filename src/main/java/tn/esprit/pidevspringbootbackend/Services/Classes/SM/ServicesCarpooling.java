package tn.esprit.pidevspringbootbackend.Services.Classes.SM;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Post9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingStatus;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingType;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.S.RepoPost9ach;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoPointCount;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.SM.IServiceCarpooling;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.mail.SimpleMailMessage;

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
    @Autowired
    RepoPost9ach repoPost9ach;
    @Autowired
    private CollocationOfferRepository collocationOfferRepository;
    @Autowired
    private JavaMailSender javaMailSender;


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
c.setReservationDate(new Date());

       CarpoolingOffer o = repoCarpoolingOffer.findById(idO).orElse(null);
       c.setLocation(o.getLocation());
       User u = repoUser.findById(id).orElse(null);
       PointCount pc = u.getPointCount();

       if (o == null || u == null || o.getUserO().equals(u)) {

           throw new IllegalArgumentException("L'utilisateur ne peut pas faire une demande sur sa propre offre de covoiturage.");
       }
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
        System.out.println("Received preferences: " + updatedPreferences);

        // Retrieve the user
        User user = repoUser.findById(userId).orElse(null);
        if (user == null) {
            return null; // User not found
        }

        // Retrieve existing preferences
        CarpoolingPreferences existingPreferences = user.getCarpoolingPreferences();
        if (existingPreferences == null) {
            // If existing preferences are null, create a new one
            existingPreferences = new CarpoolingPreferences();
            user.setCarpoolingPreferences(existingPreferences);
        }

        // Update existing preferences if updatedPreferences is not null
        if (updatedPreferences != null) {
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
        }

        // Save the updated preferences
        CarpoolingPreferences savedPreferences = repoCarpoolingPreferences.save(existingPreferences);
        return savedPreferences;
    }


    @Override
    public   List<CarpoolingOffer> getall(){


        return repoCarpoolingOffer.findAll();
}


    @Override

    public List<CarpoolingOffer> getAllActiveOffers() {
        List<CarpoolingOffer> allOffers = repoCarpoolingOffer.findAll();
        List<CarpoolingOffer> activeOffers = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();

        for (CarpoolingOffer offer : allOffers) {
            LocalDate dateAller = null;
            LocalDate dateRetour = null;

            if (offer.getDateAller() != null) {
                dateAller = offer.getDateAller().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            if (offer.getDateRetour() != null) {
                dateRetour = offer.getDateRetour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            boolean isActiveOffer = offer.getCarpoolingStatus() == CarpoolingStatus.Active;

            boolean isValidDateAller = (dateAller == null) || dateAller.isAfter(currentDate);

            boolean isValidDateRetour = (dateRetour == null) || dateRetour.isAfter(currentDate);

            if (isActiveOffer && isValidDateAller && isValidDateRetour) {
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
        List<CarpoolingOffer> activeOffers = allOffers.stream()
                .filter(offer -> offer.getCarpoolingStatus() == CarpoolingStatus.Active)
                .collect(Collectors.toList());
        Map<Integer, List<CarpoolingOffer>> offersByConditionsMet = new TreeMap<>(Collections.reverseOrder());

        for (CarpoolingOffer offer : activeOffers) {
            int conditionsMet = 0;

            if (preferences != null) {
                if (preferences.getLocation() != null && offer.getLocation() != null && offer.getLocation().equalsIgnoreCase(preferences.getLocation())) {
                    conditionsMet += 1000;
                }

                if (preferences.getNbPlaces() != null && preferences.getNbPlaces() > 0) {
                    if ((offer.getCarpoolingType() == CarpoolingType.Aller && offer.getPlaceDispoAller() == preferences.getNbPlaces()) ||
                            (offer.getCarpoolingType() == CarpoolingType.Retour && offer.getPlaceDispoRetour() == preferences.getNbPlaces()) ||
                            (offer.getCarpoolingType() == CarpoolingType.Aller_et_Retour && offer.getPlaceDispoAller() == preferences.getNbPlaces() && offer.getPlaceDispoRetour() == preferences.getNbPlaces())) {
                        conditionsMet++;
                    }
                }

                if (preferences.getBudget() != null && preferences.getBudget() > 0 && offer.getPrice() > 0  &&
                        offer.getPrice() >= preferences.getBudget() - 10 && offer.getPrice() <= preferences.getBudget() + 10) {
                    conditionsMet += 10;
                }

                if (preferences.getChauffage() != null && offer.getChauffage() != null && offer.getChauffage() == preferences.getChauffage()) {
                    conditionsMet++;
                }
                if (preferences.getClimatise() != null && offer.getClimatise() != null && offer.getClimatise() == preferences.getClimatise()) {
                    conditionsMet++;
                }
                if (preferences.getSmoking() != null && offer.getSmoking() != null && offer.getSmoking() == preferences.getSmoking()) {
                    conditionsMet++;
                }
                if (preferences.getRadioon() != null && offer.getRadioon() != null && offer.getRadioon() == preferences.getRadioon()) {
                    conditionsMet++;
                }
            }

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

    @Override
    public PointCount updatePointFidelite(Long idu, Long pcn) {
        User u = repoUser.findById(idu).orElse(null);
        PointCount pco = u.getPointCount();
        pco.setNbFidelite(pcn + pco.getNbFidelite());
        return repoPointCount.save(pco);
    }


    @Override
    public List<CarpoolingOffer> getCarpoolingOfferByIdUser(Long userId) {
        User user = repoUser.findById(userId).orElse(null);

        if (user == null) {
            return Collections.emptyList();
        }

        List<CarpoolingOffer> allOffers = repoCarpoolingOffer.findAll();

        List<CarpoolingOffer> userOffers = new ArrayList<>();

        for (CarpoolingOffer offer : allOffers) {
            if (offer.getUserO().equals(user)) {
                userOffers.add(offer);
            }
        }

        return userOffers;
    }

@Override
    public List<String> getNameUser(Long idCarpoolingOffer) {
        CarpoolingOffer offer = repoCarpoolingOffer.findById(idCarpoolingOffer).orElse(null);

        if (offer == null) {
            return Collections.emptyList();
        }

        List<CarpoolingRequest> requests = offer.getCarpoolingRequests();

        List<String> userNames = new ArrayList<>();

        for (CarpoolingRequest request : requests) {
            User user = request.getUserR();
            if (user != null) {
                userNames.add(user.getFirstName());
            }
        }

        return userNames;
    }


    @Override
    public List<CarpoolingRequest> getAllRequestsByOffer(Long offerId) {
        CarpoolingOffer offer = repoCarpoolingOffer.findById(offerId).orElse(null);

        if (offer == null) {
            return Collections.emptyList();
        }

        return offer.getCarpoolingRequests();
    }


    @Override
    public List<CarpoolingRequest> findCarpoolingRequestsByUserId(Long userId) {
        User user = repoUser.findById(userId).orElse(null);
        List<CarpoolingRequest> requests = new ArrayList<>();

        if (user != null) {
            List<CarpoolingRequest> allRequests = repoCarpoolingRequest.findAll();

            for (CarpoolingRequest request : allRequests) {
                if (request.getUserR().getIdUser().equals(userId)) {
                    requests.add(request);
                }
            }
        }

        return requests;
    }
    @Override
    public void removeCarrpooling(Long postId) {
        CarpoolingOffer post = repoCarpoolingOffer.findById(postId).orElse(null);
        post.setCarpoolingStatus(CarpoolingStatus.Canceled);
        repoCarpoolingOffer.save(post);
        if (post == null) {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }

        //  repoPost9ach.delete(post);

    }






    @Override
    public PointCount updateQuizPoint(Long idu) {
        User u = repoUser.findById(idu).orElse(null);
        PointCount pco = u.getPointCount();
        pco.setNbFidelite(pco.getNbFidelite() + 5);

        return repoPointCount.save(pco);
    }


    @Override
    public double getPointByuser(long idu){
        User u = repoUser.findById(idu).orElse(null);
        double pco = u.getPointCount().getNbFidelite();
        return  pco;
    }
    @Override
    public PointCount convertToPoint(Long userId, double pointFidelite, double pcn) {
        User u = repoUser.findById(userId).orElse(null);
        if (u == null) {
            return null;
        }
        PointCount pco = u.getPointCount();

        if (pco.getNbFidelite() >= pointFidelite) {
            pco.setNbPoint(pco.getNbPoint() + pcn);
            pco.setNbFidelite(pco.getNbFidelite() - pointFidelite);
            PointCount savedPointCount = repoPointCount.save(pco);
            CompletableFuture.runAsync(() -> sendEmail(u.getEmail(), u.getUsername(), pco.getNbPoint()));
            return savedPointCount;
        } else {
            return null;
        }
    }

    @Override
    public List<Object> getAllAccueil() {
        System.out.println("111");
        List<Object> accueil = new ArrayList<>();
        System.out.println("111222");
        List  <CollocationOffer> collocationOffers = collocationOfferRepository.findAll();
        List<CarpoolingOffer> carpoolingOffers = repoCarpoolingOffer.findByCarpoolingStatus(CarpoolingStatus.Active);
        List<Post9ach> meubles = repoPost9ach.findAll();
        // Ajouter tous les éléments à accueil
        accueil.addAll(collocationOffers);
        accueil.addAll(carpoolingOffers);
        accueil.addAll(meubles);
        // Trier la liste accueil par date d'offre
        Collections.sort(accueil, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                Date date1 = getDate(o1);
                Date date2 = getDate(o2);

                // Comparer les dates avec la date système
                long diff1 = Math.abs(date1.getTime() - new Date().getTime());
                long diff2 = Math.abs(date2.getTime() - new Date().getTime());

                // Pour un ordre décroissant en fonction de la différence avec la date système
                return Long.compare(diff1, diff2);
            }

            private Date getDate(Object obj) {
                if (obj instanceof CollocationOffer) {
                    return ((CollocationOffer) obj).getDateOffer();
                } else if (obj instanceof CarpoolingOffer) {
                    return ((CarpoolingOffer) obj).getOfferDate();
                } else if (obj instanceof Post9ach) {
                    return ((Post9ach) obj).getOfferDate();
                }
                return null;
            }
        });
        return accueil;
    }










    private void sendEmail(String userEmail, String username, double points) {
        System.out.println("Sending email to: " + userEmail);

        String emailContent = String.format("Dear %s,\n\nCongratulations! You just got %d points. Thanks for your confidence.\n\nEquipe Co&Co", username, points);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Congratulations! Transfer Points.");
        message.setText(emailContent);

        javaMailSender.send(message);

        System.out.println("Email sent successfully.");
    }

}
