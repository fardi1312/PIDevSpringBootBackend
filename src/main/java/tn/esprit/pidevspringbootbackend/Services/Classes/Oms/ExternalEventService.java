package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.ExternalEvent;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.ExternalEventRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalEventService {

    @Autowired
    private ExternalEventRepository externalEventRepository ;
    public List<ExternalEvent> getAllExternalEvents() {
        return externalEventRepository.findAll();
    }

    // Method to retrieve an external event by its ID
    public Optional<ExternalEvent> getExternalEventById(Long id) {
        return externalEventRepository.findById(id);
    }

    public ExternalEvent createExternalEvent(ExternalEvent externalEvent) {
        return externalEventRepository.save(externalEvent);
    }

    // Method to update an existing external event
    public ExternalEvent updateExternalEvent(Long id, ExternalEvent updatedEvent) {
        Optional<ExternalEvent> existingEventOptional = externalEventRepository.findById(id);
        if (existingEventOptional.isPresent()) {
            ExternalEvent existingEvent = existingEventOptional.get();
            existingEvent.setTitle(updatedEvent.getTitle());
            existingEvent.setStart(updatedEvent.getStart());
            existingEvent.setEnd(updatedEvent.getEnd());
            existingEvent.setDraggable(updatedEvent.isDraggable());
            return externalEventRepository.save(existingEvent);
        } else {
            throw new RuntimeException("External event not found with id: " + id);
        }
    }

    public void deleteExternalEvent(Long id) {
        Optional<ExternalEvent> eventOptional = externalEventRepository.findById(id);
        if (eventOptional.isPresent()) {
            externalEventRepository.deleteById(id);
        } else {
            throw new RuntimeException("External event not found with id: " + id);
        }
    }



}
