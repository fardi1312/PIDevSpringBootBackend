package tn.esprit.pidevspringbootbackend.RestControllers.Oms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationOfferServices;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collocationoffers")
public class CollocationRest {
    @Autowired
    private  CollocationOfferServices collocationOfferServices;


    @GetMapping
    public ResponseEntity<List<CollocationOffer>> getAllCollocationOffers() {
        List<CollocationOffer> collocationOffers = collocationOfferServices.getAllCollocationOffers();
        return new ResponseEntity<>(collocationOffers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollocationOffer> getCollocationOfferById(@PathVariable long id) {
        Optional<CollocationOffer> collocationOffer = collocationOfferServices.getCollocationOfferById(id);
        return collocationOffer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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

}
