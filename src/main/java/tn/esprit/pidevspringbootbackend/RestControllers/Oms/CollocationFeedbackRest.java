package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationFeedback;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationFeedbackRepository;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationFeedbackService;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationOfferServices;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;

import java.util.List;

@RestController
@RequestMapping("/api/collocation-feedback")
@CrossOrigin
public class CollocationFeedbackRest {
    @Autowired
    private CollocationFeedbackService collocationFeedbackService;
    @Autowired
    private CollocationFeedbackRepository collocationFeedbackRepository ;
    @Autowired
    private IUserService userService;


    @PostMapping("/{userId}/{offerId}")
    public ResponseEntity<CollocationFeedback> createCollocationFeedback(@RequestBody CollocationFeedback collocationFeedback,
                                                                         @PathVariable long userId,
                                                                         @PathVariable long offerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        CollocationFeedback createdCollocationFeedback = collocationFeedbackService.createCollocationFeedback(collocationFeedback,user.getIdUser(), offerId);
        return new ResponseEntity<>(createdCollocationFeedback, HttpStatus.CREATED);
    }
    public ResponseEntity<CollocationFeedback> getCollocationFeedbackById(@PathVariable long id) {
        CollocationFeedback collocationFeedback = collocationFeedbackRepository.findById(id).get();
        if (collocationFeedback == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(collocationFeedback, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CollocationFeedback>> getAllCollocationFeedback() {
        List<CollocationFeedback> collocationFeedbackList = collocationFeedbackService.getAllCollocationFeedback();
        return new ResponseEntity<>(collocationFeedbackList, HttpStatus.OK);
    }


    @PutMapping("/{id}/{userId}")
    public ResponseEntity<CollocationFeedback> updateCollocationFeedback(@PathVariable long id,
                                                                         @RequestBody CollocationFeedback updatedCollocationFeedback,
                                                                         @PathVariable long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        CollocationFeedback collocationFeedback = collocationFeedbackService.updateCollocationFeedback(id, updatedCollocationFeedback, user.getIdUser());
        return new ResponseEntity<>(collocationFeedback, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollocationFeedback(@PathVariable long id) {
        collocationFeedbackService.deleteCollocationFeedback(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

