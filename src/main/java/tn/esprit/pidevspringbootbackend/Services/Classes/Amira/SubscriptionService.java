package tn.esprit.pidevspringbootbackend.Services.Classes.Amira;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Registration;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Subscription;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Amira.RegistrationRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Amira.SubscriptionRepository;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Amira.ISubscriptionService;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService implements ISubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }
    @Override
    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }
    @Override
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }
    @Override
    public Optional<Subscription> updateSubscription(Long id, Subscription subscriptionDetails) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);
        if (optionalSubscription.isPresent()) {
            Subscription existingSubscription = optionalSubscription.get();
            existingSubscription.setName(subscriptionDetails.getName());

            return Optional.of(subscriptionRepository.save(existingSubscription));
        } else {
            return Optional.empty();
        }
    }
    @Override

    public void deleteSubscription(Long id) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);
        if (optionalSubscription.isPresent()) {
            subscriptionRepository.deleteById(id);
        } else {
        }
    }
}
