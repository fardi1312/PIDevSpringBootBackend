package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.RoomDetails;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationRequestRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.RoomDetailsRepository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class CollocationOfferServices {
    @Autowired
    private  CollocationOfferRepository collocationOfferRepository;
    @Autowired
    private CollocationRequestRepository collocationRequestRepository;
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private RoomDetailsRepository roomDetailsRepository ;

    public CollocationOffer saveCollocationOffer(CollocationOffer collocationOffer) {
        return collocationOfferRepository.save(collocationOffer);
    }

    public Optional<CollocationOffer> getCollocationOfferById(long id) {
        return collocationOfferRepository.findById(id);
    }

    public List<CollocationOffer> getAllCollocationOffers() {
        return collocationOfferRepository.findAll();
    }

    public void deleteCollocationOffer(long id) {
        collocationOfferRepository.deleteById(id);
    }
    public boolean acceptRequest(long offerId, long requestId) {
        CollocationOffer collocationOffer = collocationOfferRepository.getReferenceById(offerId);

        List<CollocationRequest> collocationRequests = collocationOffer.getCollocationRequests();

        Iterator<CollocationRequest> iterator = collocationRequests.iterator();

        while (iterator.hasNext()) {
            CollocationRequest request = iterator.next();

            if (request.getIdCollocationRequest() == requestId) {
                request.setRequest(Request.Approved);
                collocationOffer.setAvailablePlaces(collocationOffer.getAvailablePlaces() - 1);
                collocationRequestRepository.save(request);

                RoomDetails roomDetailsToRemove = collocationOffer.getRoomDetailsList().stream()
                        .filter(roomDetails -> roomDetails.getCollocationRequest().getIdCollocationRequest() == requestId)
                        .findFirst()
                        .orElse(null);

                if (roomDetailsToRemove != null) {
                    collocationOffer.getRoomDetailsList().remove(roomDetailsToRemove);
                    roomDetailsRepository.delete(roomDetailsToRemove);
                }

                return true;
            }
        }

        return false;
    }
    public boolean RefuseColloaction(long idOffer , long idRequest) {
       CollocationRequest collocationRequest =  collocationRequestRepository.getReferenceById(idRequest);
       if (collocationRequest != null){
        collocationRequestRepository.getReferenceById(idRequest).setRequest(Request.Canceled);
        collocationRequestRepository.save(collocationRequest);
        return true ; }
       else { return false ;
       }
    }
    //add offer and Associate to a certain user
    public CollocationOffer saveCollocationOfferAndAssociateUser(CollocationOffer collocationOffer, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId)) ;

        List<CollocationOffer> userOffers = user.getCollocationOffers();

        collocationOffer.setUser(user);

        userOffers.add(collocationOffer);

        user.setCollocationOffers(userOffers);

        return collocationOfferRepository.save(collocationOffer);
    }



}


