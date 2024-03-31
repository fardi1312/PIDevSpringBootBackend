package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationFeedback;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationFeedbackService;

import java.util.List;

@RestController
@RequestMapping("/api/collocation-feedback")
@CrossOrigin
public class CollocationFeedbackRest {
    @Autowired
    private CollocationFeedbackService collocationFeedbackService;

    @GetMapping
    public ResponseEntity<List<CollocationFeedback>> getAllCollocationFeedback() {
        List<CollocationFeedback> collocationFeedbackList = collocationFeedbackService.getAllCollocationFeedback();
        return new ResponseEntity<>(collocationFeedbackList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollocationFeedback> getCollocationFeedbackById(@PathVariable long id) {
        CollocationFeedback collocationFeedback = collocationFeedbackService.getCollocationFeedbackById(id);
        return new ResponseEntity<>(collocationFeedback, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CollocationFeedback> createCollocationFeedback(@RequestBody CollocationFeedback collocationFeedback,@PathVariable long id) {
        CollocationFeedback createdCollocationFeedback = collocationFeedbackService.createCollocationFeedback(collocationFeedback,id);
        return new ResponseEntity<>(createdCollocationFeedback, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollocationFeedback> updateCollocationFeedback(@PathVariable long id, @RequestBody CollocationFeedback updatedCollocationFeedback) {
        CollocationFeedback collocationFeedback = collocationFeedbackService.updateCollocationFeedback(id, updatedCollocationFeedback);
        return new ResponseEntity<>(collocationFeedback, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollocationFeedback(@PathVariable long id) {
        collocationFeedbackService.deleteCollocationFeedback(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
