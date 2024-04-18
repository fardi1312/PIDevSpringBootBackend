package tn.esprit.pidevspringbootbackend.RestControllers.Oms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationOfferServices;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationRequestService;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.calendarEvent ;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collocation")
@CrossOrigin("*")

public class CollocationOfferRest {
    @Autowired
    private  CollocationOfferServices collocationOfferServices;


    @GetMapping
    public ResponseEntity<List<CollocationOffer>> getAllCollocationOffers() {
        List<CollocationOffer> collocationOffers = collocationOfferServices.getAllCollocationOffers();
        return new ResponseEntity<>(collocationOffers, HttpStatus.OK);
    }
    @PostMapping("/send-mail/{offerId}/{requestId}")
    public ResponseEntity<String> sendMail(
            @PathVariable long offerId,
            @PathVariable long requestId) {

        boolean success = collocationOfferServices.SendMail(offerId, requestId);
        if (success) {
            return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<CollocationOffer> getCollocationOfferById(@PathVariable long id) {
        Optional<CollocationOffer> collocationOffer = collocationOfferServices.getCollocationOfferById(id);
        return collocationOffer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/{offerId}/{requestId}")
    public ResponseEntity<String> acceptCollocationRequest(@PathVariable("offerId") long offerId, @PathVariable("requestId") long requestId) {
        boolean result = collocationOfferServices.acceptRequest(offerId, requestId);

        if (result) {
            return ResponseEntity.ok("Collocation request accepted successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to accept collocation request");
        }
    }
    @PutMapping("/refuse/{offerId}/{requestId}")
    public ResponseEntity<String> refuseCollocationRequest(@PathVariable("offerId") long offerId, @PathVariable("requestId") long requestId) {
        boolean result = collocationOfferServices.refuseRequest(offerId, requestId);

        if (result) {
            return ResponseEntity.ok("Collocation request refused successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to refuse collocation request");
        }
    }


        @PostMapping
    public ResponseEntity<CollocationOffer> createCollocationOffer(@RequestBody CollocationOffer collocationOffer) {
        CollocationOffer createdCollocationOffer = collocationOfferServices.saveCollocationOffer(collocationOffer);
        return new ResponseEntity<>(createdCollocationOffer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollocationOffer> updateCollocationOffer(
            @PathVariable long id,
            @RequestBody CollocationOffer updatedCollocationOffer
    ) {
        Optional<CollocationOffer> existingCollocationOffer = collocationOfferServices.getCollocationOfferById(id);

        if (existingCollocationOffer.isPresent()) {
            updatedCollocationOffer.setIdCollocationOffer(id);
            CollocationOffer updated = collocationOfferServices.saveCollocationOffer(updatedCollocationOffer);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollocationOffer(@PathVariable long id) {
        Optional<CollocationOffer> collocationOffer = collocationOfferServices.getCollocationOfferById(id);

        if (collocationOffer.isPresent()) {
            collocationOfferServices.deleteCollocationOffer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /*@PostMapping("/offers/accept/{offerId}/request/{requestId}")
    public ResponseEntity<Void> acceptCollocationRequest(
            @PathVariable long offerId,
            @PathVariable long requestId) {

        boolean accepted = collocationOfferServices.acceptRequest(offerId, requestId);

        if (accepted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

    @PostMapping("/{userId}")
    public ResponseEntity<CollocationOffer> createCollocationOfferAndAssociateUser(
            @PathVariable long userId,
            @RequestBody CollocationOffer collocationOffer) {

        CollocationOffer createdOffer = collocationOfferServices.saveCollocationOfferAndAssociateUser(collocationOffer, userId);

        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CollocationOffer>> getCollocationOffersByUserId(@PathVariable long userId) {
        List<CollocationOffer> collocationOffers = collocationOfferServices.getAllCollocationOffersByUserId(userId);
        if (collocationOffers != null) {
            return ResponseEntity.ok(collocationOffers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    }



