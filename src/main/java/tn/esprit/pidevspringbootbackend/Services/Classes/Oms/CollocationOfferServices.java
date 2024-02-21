package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CollocationOfferServices {
    @Autowired
    private  CollocationOfferRepository collocationOfferRepository;

    public CollocationOffer saveCollocationOffer(CollocationOffer collocationOffer) {
        return collocationOfferRepository.save(collocationOffer);
    }

    public Optional<CollocationOffer> getCollocationOfferById(long id) {
        return collocationOfferRepository.findById(id);
    }

    public List<CollocationOffer> getAllCollocationOffers() {
        return collocationOfferRepository.findAll();
    }

    public void deleteCollocationOffer(long id) {
        collocationOfferRepository.deleteById(id);
    }
}


