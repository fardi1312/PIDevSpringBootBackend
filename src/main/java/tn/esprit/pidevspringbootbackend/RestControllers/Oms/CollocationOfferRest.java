package tn.esprit.pidevspringbootbackend.RestControllers.Oms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PutMapping("/{offerId}/{requestId}")
    public ResponseEntity<String> acceptCollocationRequest(@PathVariable("offerId") long offerId, @PathVariable("requestId") long requestId) {
        boolean result = collocationOfferServices.acceptRequest(offerId, requestId);

        if (result) {
            return ResponseEntity.ok("Collocation request accepted successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to accept collocation request");
        }
    }

    @PostMapping("/offers/create")
    public ResponseEntity<CollocationOffer> createCollocationOffer(
            @RequestBody CollocationOffer collocationOffer) {
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



    @GetMapping("/matchuser/{userId}")
    public ResponseEntity<List<CollocationOffer>> getMatchingOffersForUser(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        List<CollocationOffer> matchingOffers = matchingService.getMatchingOffersForUser(user.getIdUser());
        return ResponseEntity.ok(matchingOffers);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CollocationOffer>> getCollocationOffersByUserId(@PathVariable long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());

        List<CollocationOffer> collocationOffers = collocationOfferServices.getAllCollocationOffersByUserId(user.getIdUser());
        if (collocationOffers != null) {
            return ResponseEntity.ok(collocationOffers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping("/uploadImage/{id}")
    public CollocationOffer  updatenImage(@PathVariable("id") Long idCC, @RequestParam("image") MultipartFile image) {
        return collocationOfferServices.updatePostImage(idCC, image);
    }




    @GetMapping("/getImage/{id}")
        public String getCompetitionImage(@PathVariable("id") Long idCc){
        return collocationOfferServices.getImageUrlForCovByID(idCc);
    }



}



