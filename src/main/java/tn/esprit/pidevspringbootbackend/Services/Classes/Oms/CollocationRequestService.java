package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationRequestRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

    @Service
    public class CollocationRequestService {
    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private  CollocationRequestRepository collocationRequestRepository;


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


    public CollocationRequest saveCollocationRequest(CollocationRequest collocationRequest,long idUser) {
        collocationRequest.setRequest(Request.Pending);
        User user = userRepository.findById(idUser).get();
        List<CollocationRequest> requests = user.getCollocationRequests();
        requests.add(collocationRequest);
        userRepository.save(user);
        return collocationRequestRepository.save(collocationRequest);
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
    public void deleteCollocationRequest(long userId, long collocationRequestId) {
        // Find the user by id
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            List<CollocationRequest> requests = user.getCollocationRequests();

            Optional<CollocationRequest> collocationRequestOptional = requests.stream()
                    .filter(request -> request.getIdCollocationRequest() == collocationRequestId)
                    .findFirst();

            collocationRequestOptional.ifPresent(requests::remove);

            collocationRequestRepository.deleteById(collocationRequestId);

            userRepository.save(user);
        }
    }

}
