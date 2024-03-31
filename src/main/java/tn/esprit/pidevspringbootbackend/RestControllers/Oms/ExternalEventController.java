package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.ExternalEvent;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.ExternalEventService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/external-events")
@CrossOrigin

public class ExternalEventController {

    @Autowired
    private ExternalEventService externalEventService;

    @GetMapping
    public ResponseEntity<List<ExternalEvent>> getAllExternalEvents() {
        List<ExternalEvent> externalEvents = externalEventService.getAllExternalEvents();
        return ResponseEntity.ok(externalEvents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExternalEvent> getExternalEventById(@PathVariable Long id) {
        Optional<ExternalEvent> externalEvent = externalEventService.getExternalEventById(id);
        return externalEvent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExternalEvent> createExternalEvent(@RequestBody ExternalEvent externalEvent) {
        ExternalEvent createdEvent = externalEventService.createExternalEvent(externalEvent);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExternalEvent> updateExternalEvent(@PathVariable Long id,
                                                             @RequestBody ExternalEvent updatedEvent) {
        ExternalEvent updated = externalEventService.updateExternalEvent(id, updatedEvent);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExternalEvent(@PathVariable Long id) {
        externalEventService.deleteExternalEvent(id);
        return ResponseEntity.noContent().build();
    }
}

