package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationRequestService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collocationRequest")

public class CollocationRequestRest {
@Autowired
private CollocationRequestService collocationRequestService;

    @GetMapping
    public ResponseEntity<List<CollocationRequest>> getAllCollocationRequests() {
        List<CollocationRequest> collocationRequests = collocationRequestService.getAllCollocationRequests();
        return new ResponseEntity<>(collocationRequests, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollocationRequest> getCollocationRequestById(@PathVariable long id) {
        Optional<CollocationRequest> collocationRequest = collocationRequestService.getCollocationRequestById(id);
        return collocationRequest.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CollocationRequest> createCollocationRequest(@RequestBody CollocationRequest collocationRequest,@PathVariable long idUser) {
        CollocationRequest createdCollocationRequest = collocationRequestService.saveCollocationRequest(collocationRequest,idUser);
        return new ResponseEntity<>(createdCollocationRequest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollocationRequest> updateCollocationRequest(@PathVariable long id, @RequestBody CollocationRequest updatedCollocationRequest,@PathVariable long idUser) {
        Optional<CollocationRequest> existingCollocationRequest = collocationRequestService.getCollocationRequestById(id);

        if (existingCollocationRequest.isPresent()) {
            updatedCollocationRequest.setIdCollocationRequest(id);
            CollocationRequest updated = collocationRequestService.saveCollocationRequest(updatedCollocationRequest,idUser);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollocationRequest(@PathVariable long id,@PathVariable long idUser) {
        Optional<CollocationRequest> collocationRequest = collocationRequestService.getCollocationRequestById(id);

        if (collocationRequest.isPresent()) {
            collocationRequestService.deleteCollocationRequest(id,idUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
