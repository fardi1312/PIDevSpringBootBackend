package tn.esprit.pidevspringbootbackend.Services.Interfaces.Amira;


import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Subscription;

import java.util.List;
import java.util.Optional;

public interface ISubscriptionService {

    List<Subscription> getAllSubscriptions();

    Optional<Subscription> getSubscriptionById(Long id);

    Subscription createSubscription(Subscription subscription);

    Optional<Subscription> updateSubscription(Long id, Subscription subscriptionDetails);

    void deleteSubscription(Long id);
}
