package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationFeedback;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.RoomDetails;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationRequestRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.RoomDetailsRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

    @Service
    public class CollocationRequestService {
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    RoomDetailsRepository roomDetailsRepository ;
    @Autowired
    private  CollocationRequestRepository collocationRequestRepository;
    @Autowired
    private CollocationOfferRepository collocationOfferRepository ;


    public List<CollocationRequest> getAllCollocationRequests() {
        return collocationRequestRepository.findAll();
    }

    public Optional<CollocationRequest> getCollocationRequestById(long id) {
        return collocationRequestRepository.findById(id);
    }

    public List<CollocationRequest> getCollocationRequestsByIdUser(long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getCollocationRequests();
        } else {
            return Collections.emptyList();
        }
    }
        public List<CollocationRequest> getCollocationRequestsByOfferId(long offerId) {
            CollocationOffer collocationOffer = collocationOfferRepository.findById(offerId).orElse(null);
            if (collocationOffer != null) {
                return collocationOffer.getCollocationRequests();
            } else {
                return null;
            }
    }



            public CollocationRequest saveCollocationRequest(CollocationRequest collocationRequest, long id) {
            collocationRequest.setRequest(Request.Pending);
            collocationRequest.setDate(new Date());

            // Associate collocation offer with the request
            CollocationOffer collocationOffer = collocationOfferRepository.findById(id).orElse(null);
            if (collocationOffer != null) {
                collocationRequest.setCollocationOffer(collocationOffer);
                collocationOffer.getCollocationRequests().add(collocationRequest);
                collocationOfferRepository.save(collocationOffer);
                System.out.println("Associated with offer");
            }


            return collocationRequestRepository.save(collocationRequest);
        }

        public CollocationRequest saveCollocationRequest(CollocationRequest collocationRequest, long id, long userId) {
            collocationRequest.setRequest(Request.Pending);
            collocationRequest.setDate(new Date());

            // Find the collocation offer by its ID
            CollocationOffer collocationOffer = collocationOfferRepository.findById(id).orElse(null);

            if (collocationOffer != null) {
                // Set the associated collocation offer
                collocationRequest.setCollocationOffer(collocationOffer);

                // Associate the collocation request with the collocation offer
                collocationOffer.getCollocationRequests().add(collocationRequest);
                collocationRequest.setRoomDetailsList(collocationRequest.getRoomDetailsList());
                collocationRequest.setUser(userRepository.findByIdUser(userId));


                // Save the collocation offer first to ensure it's persisted
                collocationOfferRepository.save(collocationOffer);

                // Save the collocation request
                return collocationRequestRepository.save(collocationRequest);
            } else {
                // Handle the case where the collocation offer is not found
                return null;
            }
        }

        public CollocationRequest updateCollocationRequest(CollocationRequest collocationRequest,long id) {
        CollocationRequest collocationRequest1 = collocationRequestRepository.findById(id).get();
        collocationRequest1.setIdCollocationRequest(collocationRequest.getIdCollocationRequest());
        collocationRequest1.setPlaces(collocationRequest.getPlaces());
        collocationRequest1.setDescription(collocationRequest.getDescription());
        collocationRequest1.setHouseType(collocationRequest.getHouseType());
        collocationRequest1.setRoomDetailsList(collocationRequest.getRoomDetailsList());
        collocationRequest1.setSelectedDate(collocationRequest.getSelectedDate());
        return collocationRequestRepository.save(collocationRequest1);
}
    public CollocationRequest updateCollocationRequest(CollocationRequest updatedCollocationRequest, long idUser, long idRequest) {
        User user = userRepository.findById(idUser).orElse(null);

        if (user != null) {
            List<CollocationRequest> requests = user.getCollocationRequests();

            Optional<CollocationRequest> collocationRequestOptional = requests.stream()
                    .filter(request -> request.getIdCollocationRequest() == idRequest)
                    .findFirst();

            if (collocationRequestOptional.isPresent()) {
                int index = requests.indexOf(collocationRequestOptional.get());
                requests.set(index, updatedCollocationRequest);

                userRepository.save(user);

                return updatedCollocationRequest;
            }
        }

        return null;
    }
    public void deleteCollocationRequest( long collocationRequestId) {

            collocationRequestRepository.deleteById(collocationRequestId);
        }


}
