package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationFeedback;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationFeedbackRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;

import java.util.List;

@Service
public class CollocationFeedbackService {
    @Autowired
    private CollocationFeedbackRepository collocationFeedbackRepository;
    @Autowired
    private CollocationOfferRepository collocationOfferRepository;
    @Autowired
    private UserRepository userRepository;

    // Method to create a new collocation feedback associated with a specific user
    public CollocationFeedback createCollocationFeedback(CollocationFeedback collocationFeedback, long userId, long offerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        CollocationOffer collocationOffer = collocationOfferRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException("CollocationOffer not found with id: " + offerId));

        collocationFeedback.setUser(user);
        collocationFeedback.setCollocationOffer(collocationOffer);

        collocationOffer.getCollocationFeedbacks().add(collocationFeedback);
        collocationOfferRepository.save(collocationOffer);

        return collocationFeedbackRepository.save(collocationFeedback);
    }

    // Method to update an existing collocation feedback associated with a specific user
    public CollocationFeedback updateCollocationFeedback(long id, CollocationFeedback updatedCollocationFeedback, long userId) {
        CollocationFeedback existingCollocationFeedback = collocationFeedbackRepository.findById(id).get();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(("User not found with id: " + userId)));

        existingCollocationFeedback.setFeedbackDate(updatedCollocationFeedback.getFeedbackDate());
        existingCollocationFeedback.setFeedbackDescription(updatedCollocationFeedback.getFeedbackDescription());
        existingCollocationFeedback.setRating(updatedCollocationFeedback.getRating());
        existingCollocationFeedback.setCollocationOffer(updatedCollocationFeedback.getCollocationOffer());
        existingCollocationFeedback.setUser(user);

        return collocationFeedbackRepository.save(existingCollocationFeedback);
    }

    // Method to delete a collocation feedback associated with a specific user
    public void deleteCollocationFeedback(long id) {
        collocationFeedbackRepository.deleteById(id);
    }
    public List<CollocationFeedback> getAllCollocationFeedback() {
        return collocationFeedbackRepository.findAll();
    }

}