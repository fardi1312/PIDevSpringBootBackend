package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;


import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationFeedback;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationFeedbackRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;

import java.util.List;

@Service

public class CollocationFeedbackService {
    @Autowired
    private CollocationFeedbackRepository collocationFeedbackRepository;
    @Autowired
    private CollocationOfferRepository collocationOfferRepository ;

    public List<CollocationFeedback> getAllCollocationFeedback() {
        return collocationFeedbackRepository.findAll();
    }

    public CollocationFeedback getCollocationFeedbackById(long id) {
        return collocationFeedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CollocationFeedback not found with id: " + id));
    }

    public CollocationFeedback createCollocationFeedback(CollocationFeedback collocationFeedback, long id) {
        System.out.println("adding ... ");
        CollocationOffer collocationOffer = collocationOfferRepository.getReferenceById(id);
        System.out.println("adding ... ");


        // Initialize lazy-loaded associations

        // Associate the feedback with the offer
        collocationFeedback.setCollocationOffer(collocationOffer);
        System.out.println("adding ... ");


        // Add the feedback to the offer's feedback list
        collocationOffer.getCollocationFeedbacks().add(collocationFeedback);


        // Save the updated offer with the new feedback
        collocationOfferRepository.save(collocationOffer);


        // Save the feedback
        return collocationFeedbackRepository.save(collocationFeedback);
    }

    public CollocationFeedback updateCollocationFeedback(long id, CollocationFeedback updatedCollocationFeedback) {
        CollocationFeedback existingCollocationFeedback = getCollocationFeedbackById(id);
        existingCollocationFeedback.setFeedbackDate(updatedCollocationFeedback.getFeedbackDate());
        existingCollocationFeedback.setFeedbackDescription(updatedCollocationFeedback.getFeedbackDescription());
        existingCollocationFeedback.setRating(updatedCollocationFeedback.getRating());
        existingCollocationFeedback.setCollocationOffer(updatedCollocationFeedback.getCollocationOffer());
        existingCollocationFeedback.setUsers(updatedCollocationFeedback.getUsers());

        return collocationFeedbackRepository.save(existingCollocationFeedback);
    }

    public void deleteCollocationFeedback(long id) {
        collocationFeedbackRepository.deleteById(id);
    }


}
