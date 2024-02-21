package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationFeedback;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationFeedbackRepository;

import java.util.List;

@Service

public class CollocationFeedbackService {
    @Autowired
    private CollocationFeedbackRepository collocationFeedbackRepository;

    public List<CollocationFeedback> getAllCollocationFeedback() {
        return collocationFeedbackRepository.findAll();
    }

    public CollocationFeedback getCollocationFeedbackById(long id) {
        return collocationFeedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CollocationFeedback not found with id: " + id));
    }

    public CollocationFeedback createCollocationFeedback(CollocationFeedback collocationFeedback) {
        return collocationFeedbackRepository.save(collocationFeedback);
    }

    public CollocationFeedback updateCollocationFeedback(long id, CollocationFeedback updatedCollocationFeedback) {
        CollocationFeedback existingCollocationFeedback = getCollocationFeedbackById(id);
        existingCollocationFeedback.setFeedbackDate(updatedCollocationFeedback.getFeedbackDate());
        existingCollocationFeedback.setFeedbackDescription(updatedCollocationFeedback.getFeedbackDescription());
        existingCollocationFeedback.setRating(updatedCollocationFeedback.getRating());
        existingCollocationFeedback.setReviewerName(updatedCollocationFeedback.getReviewerName());
        existingCollocationFeedback.setReviewerEmail(updatedCollocationFeedback.getReviewerEmail());
        existingCollocationFeedback.setCollocationOffer(updatedCollocationFeedback.getCollocationOffer());
        existingCollocationFeedback.setUsers(updatedCollocationFeedback.getUsers());

        return collocationFeedbackRepository.save(existingCollocationFeedback);
    }

    public void deleteCollocationFeedback(long id) {
        collocationFeedbackRepository.deleteById(id);
    }


}
