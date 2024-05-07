package tn.esprit.pidevspringbootbackend.Services.Interfaces.Maram;

import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.EventRequests;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.util.List;

public interface IEventRequestService {
    List<EventRequests> getAllEventRequests();
    EventRequests getEventRequestById(Long id);
    EventRequests saveOrUpdateEventRequest(EventRequests eventRequests, User user);
    void deleteEventRequest(Long id);
}

