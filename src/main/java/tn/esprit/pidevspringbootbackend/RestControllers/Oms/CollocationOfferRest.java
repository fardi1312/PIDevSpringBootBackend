package tn.esprit.pidevspringbootbackend.RestControllers.Oms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationOfferServices;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.MatchingService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collocation")
@CrossOrigin("*")

public class CollocationOfferRest {
    @Autowired
    private  CollocationOfferServices collocationOfferServices;
    @Autowired
    private MatchingService matchingService;
    @Autowired
    private IUserService userService;

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



    @PutMapping("/{id}")
    public ResponseEntity<CollocationOffer> updateCollocationOffer(@PathVariable long id, @RequestBody CollocationOffer updatedCollocationOffer) {
        CollocationOffer updatedOffer = collocationOfferServices.updateCollocationOffer(id, updatedCollocationOffer);
        return ResponseEntity.ok(updatedOffer);
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
    @PostMapping("/offers/accept/{offerId}/request/{requestId}")
    public ResponseEntity<Void> acceptCollocationRequest(
            @PathVariable long offerId,
            @PathVariable long requestId) {

        boolean accepted = collocationOfferServices.acceptRequest(offerId, requestId);

        if (accepted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/offers/create")
    public ResponseEntity<CollocationOffer> createCollocationOffer(
            @RequestBody CollocationOffer collocationOffer,
            @RequestParam long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        CollocationOffer createdOffer = collocationOfferServices.saveCollocationOfferAndAssociateUser(collocationOffer, user.getIdUser());

        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }
//    @PostMapping("/toggle-saved-status/{offerId}")
//    public ResponseEntity<Void> toggleOfferSavedStatus(@PathVariable Long offerId, @RequestParam Long userId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.getUserByEmail(authentication.getName());
//        collocationOfferServices.toggleOfferSavedStatus(offerId, user.getIdUser());
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/search")
    public ResponseEntity<List<CollocationOffer>> search(
            @RequestParam(required = false) String governorate,
            @RequestParam(required = false) Integer houseType,
            @RequestParam(required = false) Integer availablePlaces,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateRent
    ) {
        List<CollocationOffer> offers = collocationOfferServices.search(governorate, houseType, availablePlaces, dateRent);
        return ResponseEntity.ok(offers);
    }
    @GetMapping("getCollocationImage/{id}")
    public String getCompetitionImage(@PathVariable("id") int idCollocation){
        return collocationOfferServices.getImageUrlForCompetitionByID(idCollocation);
    }


    @GetMapping("/matchuser/{userId}")
    public ResponseEntity<List<CollocationOffer>> getMatchingOffersForUser(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        List<CollocationOffer> matchingOffers = matchingService.getMatchingOffersForUser(user.getIdUser());
        return ResponseEntity.ok(matchingOffers);
    }
}



