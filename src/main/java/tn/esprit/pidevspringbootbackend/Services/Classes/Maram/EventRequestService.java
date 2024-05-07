package tn.esprit.pidevspringbootbackend.Services.Classes.Maram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.EventRequests;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Maram.EventRequestRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Maram.IEventRequestService;

import java.util.List;
import java.util.Optional;
@Service

public class EventRequestService implements IEventRequestService {
@Autowired
      EventRequestRepository eventRequestRepository;
    @Autowired

      UserRepository userRepository;



    @Override
    public List<EventRequests> getAllEventRequests() {
        return eventRequestRepository.findAll();
    }

    @Override
    public EventRequests getEventRequestById(Long id) {
        Optional<EventRequests> optionalEventRequest = eventRequestRepository.findById(id);
        return optionalEventRequest.orElse(null);
    }

    @Override
    public EventRequests saveOrUpdateEventRequest(EventRequests eventRequests, User user) {
        eventRequests.setUser(user); // Set the user for the event request
        return eventRequestRepository.save(eventRequests);
    }

    @Override
    public void deleteEventRequest(Long id) {
        eventRequestRepository.deleteById(id);
    }


}
