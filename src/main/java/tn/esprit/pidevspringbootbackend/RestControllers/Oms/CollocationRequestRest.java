package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.RoomDetails;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.RoomDetailsRepository;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationRequestService;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.RoomDetailsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collocationRequest")
@CrossOrigin

public class CollocationRequestRest {
@Autowired
private CollocationRequestService collocationRequestService;
@Autowired
private RoomDetailsRepository roomDetailsRepository ;

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
    @PostMapping("/{id}/{userId}")
    public ResponseEntity<CollocationRequest> associateUserWithCollocationRequest(
            @RequestBody CollocationRequest request,
            @PathVariable long userId,
            @PathVariable long id) {
        CollocationRequest collocationRequest = collocationRequestService.saveCollocationRequest(request, id, userId);
        if (collocationRequest != null) {
            return ResponseEntity.ok(collocationRequest);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/{offerId}/requests")
    public ResponseEntity<List<CollocationRequest>> getRequestsForOffer(@PathVariable("offerId") long offerId) {
        List<CollocationRequest> requests = collocationRequestService.getCollocationRequestsByOfferId(offerId);
        if (requests != null) {
            return ResponseEntity.ok(requests);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        @PostMapping("/{id}")
    public ResponseEntity<CollocationRequest> createCollocationRequest(@RequestBody CollocationRequest collocationRequest,@PathVariable long id) {
            System.out.println(collocationRequest.getSelectedDate().toString());


        CollocationRequest createdCollocationRequest = collocationRequestService.saveCollocationRequest(collocationRequest,id);

        return new ResponseEntity<>(createdCollocationRequest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollocationRequest> updateCollocationRequest(@PathVariable long id, @RequestBody CollocationRequest updatedCollocationRequest) {
        CollocationRequest updatedRequest = collocationRequestService.updateCollocationRequest(updatedCollocationRequest, id);
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollocationRequest(@PathVariable long id) {
        Optional<CollocationRequest> collocationRequest = collocationRequestService.getCollocationRequestById(id);

        if (collocationRequest.isPresent()) {
            collocationRequestService.deleteCollocationRequest(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
